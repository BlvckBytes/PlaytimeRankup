package at.blvckbytes.playtime_rankup.store;

import at.blvckbytes.cm_mapper.ConfigKeeper;
import at.blvckbytes.playtime_rankup.config.MainSection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDataStore {

  private final CalendarInfoProvider calendarInfoProvider;
  private final Logger logger;
  private final File userDataFolder;
  private final Map<UUID, UserData> userDataByPlayerId;

  private long lastSaveStamp;
  private long lastCalendarBucketKeyUpdateStamp;

  public UserDataStore(
    CalendarInfoProvider calendarInfoProvider,
    ConfigKeeper<MainSection> config,
    Plugin plugin
  ) {
    this.calendarInfoProvider = calendarInfoProvider;
    this.logger = plugin.getLogger();
    this.userDataFolder = new File(plugin.getDataFolder(), "userdata");

    if (!userDataFolder.exists()) {
      if (!userDataFolder.mkdirs())
        throw new IllegalStateException("Could not create folder " + userDataFolder);
    }

    else if (!userDataFolder.isDirectory())
      throw new IllegalStateException("Expected a directory at " + userDataFolder);

    this.userDataByPlayerId = new HashMap<>();

    Bukkit.getScheduler().runTaskTimer(plugin, () -> {
      var now = System.currentTimeMillis();

      if (now - lastSaveStamp >= config.rootSection.saveIntervalSeconds * 1000L) {
        lastSaveStamp = now;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::saveAllDirty);
      }

      if (now - lastCalendarBucketKeyUpdateStamp >= config.rootSection.calendarBucketKeyUpdateIntervalSeconds * 1000L) {
        lastCalendarBucketKeyUpdateStamp = now;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::updateAllKeys);
      }
    }, 0, 0);

    Bukkit.getScheduler().runTaskAsynchronously(plugin, this::loadAll);
  }

  public void onDisable() {
    saveAllDirty();
  }

  public UserData access(OfflinePlayer player) {
    return access(player.getUniqueId(), player.getName());
  }

  private UserData access(UUID playerId, String playerName) {
    synchronized (userDataByPlayerId) {
      return userDataByPlayerId.computeIfAbsent(playerId, _ -> UserData.makeInitial(playerId, playerName, calendarInfoProvider));
    }
  }

  public void batchIncrementPlayTimeFor(Iterable<? extends PlayerIdentification> playerIdentifications, int value) {
    synchronized (userDataByPlayerId) {
      for (var playerIdentification : playerIdentifications)
        access(playerIdentification.getPlayerId(), playerIdentification.getPlayerName()).incrementPlayTimeTicks(value, calendarInfoProvider);
    }
  }

  public void batchIncrementAfkTimeFor(Iterable<? extends PlayerIdentification> playerIdentifications, int value) {
    synchronized (userDataByPlayerId) {
      for (var playerIdentification : playerIdentifications)
        access(playerIdentification.getPlayerId(), playerIdentification.getPlayerName()).incrementAfkTimeTicks(value, calendarInfoProvider);
    }
  }

  private void updateAllKeys() {
    synchronized (userDataByPlayerId) {
      for (var userData : userDataByPlayerId.values()) {
        userData.updateCalendarBucketKeys(calendarInfoProvider);
      }
    }
  }

  private void saveAllDirty() {
    synchronized (userDataByPlayerId) {
      for (var userData : userDataByPlayerId.values()) {
        if (!userData.isDirty())
          continue;

        var dataFile = getDataFile(userData);

        if (dataFile.isDirectory()) {
          logger.severe("Expected a file at " + dataFile + "; skipping!");
          continue;
        }

        var dataString = userData.serialize();

        if (dataFile.exists() && dataFile.length() > 0) {
          var writtenUserData = tryLoadUserData(dataFile);

          if (writtenUserData == null) {
            logger.severe("Encountered corrupted user-data file " + dataFile + "; skipping write of " + sanitizeControlCharacters(dataString));
            continue;
          }

          // Better safe than sorry - playtime can absolutely not be lost and if we failed loading, we'll zero-initialize
          // on the first access-call, so we'd erase the player's progress; this way, a human can manually merge later on.
          if (writtenUserData.getGlobalPlayTimeTicks() > userData.getGlobalPlayTimeTicks() || writtenUserData.getGlobalAfkTimeTicks() > userData.getGlobalAfkTimeTicks()) {
            logger.severe("Stored statistics in file " + dataFile + " exceed about-to-be-saved; skipping write of " + sanitizeControlCharacters(dataString));
            continue;
          }

          var backupFile = getBackupDataFile(userData);

          try {
            copyFiles(dataFile, backupFile);
          } catch (Throwable e) {
            logger.log(Level.SEVERE, "Could not create backup-file " + backupFile + "; skipping", e);
            continue;
          }
        }

        try (var writer = new FileWriter(dataFile)) {
          writer.write(dataString);
        } catch (Throwable e) {
          logger.log(Level.SEVERE, "Could not write data-file " + dataFile, e);
        }

        userData.clearDirty();
      }
    }
  }

  private void loadAll() {
    synchronized (userDataByPlayerId) {
      var userDataFiles = userDataFolder.listFiles();

      if (userDataFiles == null) {
        logger.severe("Could not list files of directory " + userDataFolder);
        return;
      }

      for (var userDataFile : userDataFiles) {
        var userData = tryLoadUserData(userDataFile);

        if (userData != null)
          userDataByPlayerId.put(userData.playerId, userData);
      }
    }
  }

  private @Nullable UserData tryLoadUserData(File userDataFile) {
    var yamlExtension = ".yml";
    var fileName = userDataFile.getName();

    if (!fileName.endsWith(yamlExtension))
      return null;

    var idString = fileName.substring(0, fileName.length() - yamlExtension.length());

    UUID playerId;

    try {
      playerId = UUID.fromString(idString);
    } catch (Throwable e) {
      logger.severe("Encountered userdata-file with invalid uuid " + userDataFile + "; skipping");
      return null;
    }

    String fileContents;

    try {
      fileContents = readFileIntoString(userDataFile);
    } catch (Throwable e) {
      logger.log(Level.SEVERE, "Could not read contents of " + userDataFile + "; skipping", e);
      return null;
    }

    try {
      return UserData.deserialize(playerId, fileContents);
    } catch (Throwable e) {
      logger.log(Level.SEVERE, "Could not deserialize contents of " + userDataFile + "; skipping", e);
    }

    return null;
  }

  private File getDataFile(UserData userData) {
    return new File(userDataFolder, userData.playerId + ".yml");
  }

  private File getBackupDataFile(UserData userData) {
    return new File(userDataFolder, userData.playerId + ".yml.bak");
  }

  private static String readFileIntoString(File source) throws IOException {
    var stringBuilder = new StringBuilder();

    try (
      var reader = new FileReader(source)
    ) {
      var buffer = new char[1024];

      int length;
      int totalLength = 0;

      while ((length = reader.read(buffer)) > 0) {
        stringBuilder.append(new String(buffer, 0, length));
        totalLength += length;

        if (totalLength >= 10 * 1024)
          throw new IllegalStateException("No userdata-file should ever exceed a size of 10k!");
      }
    }

    return stringBuilder.toString();
  }

  private static void copyFiles(File source, File destination) throws IOException {
    try (
      var inputStream = new FileInputStream(source);
      var outputStream = new FileOutputStream(destination)
    ) {
      var buffer = new byte[1024];

      int length;

      while ((length = inputStream.read(buffer)) > 0)
        outputStream.write(buffer, 0, length);
    }
  }

  private static String sanitizeControlCharacters(String input) {
    var builder = new StringBuilder(input.length());

    for (var charIndex = 0; charIndex < input.length(); ++charIndex) {
      var currentChar = input.charAt(charIndex);

      switch (currentChar) {
        case '\n':
          builder.append("\\n");
          continue;
        case '\r':
          builder.append("\\r");
          continue;
        case '\t':
          builder.append("\\t");
          continue;
        case '\b':
          builder.append("\\b");
          continue;
        case '\f':
          builder.append("\\f");
          continue;
        case '\\':
          builder.append("\\\\");
          continue;

        default:
          if (Character.isISOControl(currentChar)) {
            builder.append(String.format("\\u%04X", (int) currentChar));
            continue;
          }

          builder.append(currentChar);
      }
    }

    return builder.toString();
  }
}
