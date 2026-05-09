package at.blvckbytes.playtime_rankup.command;

import at.blvckbytes.playtime_rankup.store.CalendarBucket;
import at.blvckbytes.playtime_rankup.store.TimeType;
import at.blvckbytes.playtime_rankup.store.UserDataStore;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlaytimeCommand implements CommandExecutor, TabCompleter {

  private final UserDataStore userDataStore;
  private final OfflinePlayerRegistry offlinePlayerRegistry;

  public PlaytimeCommand(
    UserDataStore userDataStore,
    OfflinePlayerRegistry offlinePlayerRegistry
  ) {
    this.userDataStore = userDataStore;
    this.offlinePlayerRegistry = offlinePlayerRegistry;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    OfflinePlayer target;

    if (args.length == 0) {
      if (!(sender instanceof Player player)) {
        // TODO: Config-message
        sender.sendMessage("§cConsole-execution requires a name-argument");
        return true;
      }

      target = player;
    }

    else {
      if (args.length != 1) {
        // TODO: Config-message
        sender.sendMessage("§cUsage: /" + label + " [Name]");
        return true;
      }

      target = offlinePlayerRegistry.getPlayerByName(args[0]);

      if (target == null) {
        sender.sendMessage("§cThe player " + args[0] + " hasn't played on this server yet!");
        return true;
      }
    }

    var data = userDataStore.access(target);

    // TODO: Config-message (with isSelf)

    sender.sendMessage("§a" + target.getName() + "'s total playtime is " + data.getGlobalTimeTicks(TimeType.PLAY_TIME) + ", their total afk-time is " + data.getGlobalTimeTicks(TimeType.AFK_TIME));

    for (CalendarBucket calendarBucket : CalendarBucket.ALL_VALUES)
      sender.sendMessage("§a" + calendarBucket.name() + " playtime is " + data.getCalendarBucketTimeTicks(calendarBucket, TimeType.PLAY_TIME) + ", afk-time is " + data.getCalendarBucketTimeTicks(calendarBucket, TimeType.AFK_TIME));

    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      var typedNameLower = args[0].toLowerCase();

      return offlinePlayerRegistry.streamKnownNames()
        .filter(it -> it.toLowerCase().startsWith(typedNameLower))
        .limit(15)
        .toList();
    }

    return List.of();
  }
}
