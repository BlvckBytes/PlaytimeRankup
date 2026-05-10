package at.blvckbytes.playtime_rewards.store;

import at.blvckbytes.cm_mapper.ConfigKeeper;
import at.blvckbytes.playtime_rewards.config.MainSection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.LocalDate;

public class CalendarInfoProvider {

  private final ConfigKeeper<MainSection> config;

  private final int[] calendarKeyByCalendarBucketOrdinal;

  public CalendarInfoProvider(
    ConfigKeeper<MainSection> config,
    Plugin plugin
  ) {
    this.config = config;

    this.calendarKeyByCalendarBucketOrdinal = new int[CalendarBucket.ALL_VALUES.size()];

    Bukkit.getScheduler().runTaskTimer(plugin, this::updateKeys, 0, 0);

    updateKeys();
  }

  public int getCalendarKey(CalendarBucket calendarBucket) {
    return calendarKeyByCalendarBucketOrdinal[calendarBucket.ordinal()];
  }

  private void updateKeys() {
    var currentDate = LocalDate.now(config.rootSection._timeZone);

    for (var calendarBucket : CalendarBucket.ALL_VALUES)
      calendarKeyByCalendarBucketOrdinal[calendarBucket.ordinal()] = calendarBucket.makeKey(currentDate);
  }
}
