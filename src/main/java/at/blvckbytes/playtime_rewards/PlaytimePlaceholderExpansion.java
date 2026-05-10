package at.blvckbytes.playtime_rewards;

import at.blvckbytes.playtime_rewards.store.CalendarBucket;
import at.blvckbytes.playtime_rewards.store.TimeType;
import at.blvckbytes.playtime_rewards.store.TopListType;
import at.blvckbytes.playtime_rewards.store.UserDataStore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaytimePlaceholderExpansion extends PlaceholderExpansion {

  private final Plugin plugin;
  private final UserDataStore userDataStore;

  public PlaytimePlaceholderExpansion(
    Plugin plugin,
    UserDataStore userDataStore
  ) {
    this.plugin = plugin;
    this.userDataStore = userDataStore;
  }

  @Override
  public @NotNull String getIdentifier() {
    return "playtime";
  }

  @Override
  public @NotNull String getAuthor() {
    return String.join(", ", plugin.getPluginMeta().getAuthors());
  }

  @Override
  public @NotNull String getVersion() {
    return plugin.getPluginMeta().getVersion();
  }

  @Override
  public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
    var args = params.split("_");

    if (args.length < 2)
      return null;

    var userData = userDataStore.access(player);

    var timeType = switch (args[0]) {
      case "play" -> TimeType.PLAY_TIME;
      case "afk" -> TimeType.AFK_TIME;
      case null, default -> null;
    };

    if (timeType == null)
      return null;

    if (args[1].equals("global")) {
      if (args.length == 2)
        return String.valueOf(userData.getGlobalTimeTicks(timeType));

      return tryAccessTopTime(args, TopListType.GLOBAL, timeType);
    }

    var bucketType = switch (args[1]) {
      case "day" -> CalendarBucket.DAY;
      case "week" -> CalendarBucket.WEEK;
      case "month" -> CalendarBucket.MONTH;
      case "year" -> CalendarBucket.YEAR;
      default -> null;
    };

    if (bucketType == null)
      return null;

    if (args.length == 2)
      return String.valueOf(userData.getCalendarBucketTimeTicks(bucketType, timeType));

    return tryAccessTopTime(args, bucketType.getTopListType(), timeType);
  }

  private @Nullable String tryAccessTopTime(String[] args, TopListType topListType, TimeType timeType) {
    if (!(args.length == 4 || args.length == 5) || !args[2].equals("top"))
      return null;

    int topPlace;

    try {
      topPlace = Integer.parseInt(args[3]);
    } catch (Throwable e) {
      return null;
    }

    if (topPlace <= 0)
      return null;

    var topList = userDataStore.getTopList(topListType, timeType);

    if (topPlace > topList.size())
      return null;

    var targetUser = topList.get(topPlace - 1);

    if (args.length == 4) {
      var calendarBucket = topListType.getCalendarBucket();

      if (calendarBucket == null)
        return String.valueOf(targetUser.getGlobalTimeTicks(timeType));

      return String.valueOf(targetUser.getCalendarBucketTimeTicks(calendarBucket, timeType));
    }

    if (!args[4].equals("name"))
      return null;

    return targetUser.getLastKnownName();
  }
}
