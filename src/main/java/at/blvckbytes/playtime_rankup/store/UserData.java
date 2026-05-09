package at.blvckbytes.playtime_rankup.store;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.StringReader;
import java.util.UUID;

public class UserData {

  public final UUID playerId;

  private String lastKnownName;

  private final TimeStatistics globalStatistics;

  private final TimeStatisticsAndKey[] statisticsByCalendarBucketOrdinal;

  private boolean dirty;

  private UserData(
    UUID playerId,
    String lastKnownName,
    TimeStatistics globalStatistics,
    TimeStatisticsAndKey[] statisticsByCalendarBucketOrdinal
  ) {
    this.playerId = playerId;

    this.lastKnownName = lastKnownName;

    if (lastKnownName.isBlank())
      throw new IllegalStateException("Property \"lastKnownName\" is blank");

    this.globalStatistics = globalStatistics;

    this.statisticsByCalendarBucketOrdinal = statisticsByCalendarBucketOrdinal;

    if (statisticsByCalendarBucketOrdinal.length != CalendarBucket.ALL_VALUES.size())
      throw new IllegalStateException("Array \"statisticsByCalendarBucketOrdinal\" does not hold as many values as there are bucket-types");
  }

  public void updateLastKnownName(String name) {
    if (name.equals(lastKnownName))
      return;

    this.lastKnownName = name;
    this.dirty = true;
  }

  public void updateCalendarBucketKeys(CalendarInfoProvider calendarInfoProvider) {
    for (var calendarBucket : CalendarBucket.ALL_VALUES) {
      var bucketStatistics = statisticsByCalendarBucketOrdinal[calendarBucket.ordinal()];
      dirty |= bucketStatistics.updateKeyAndResetIfApplicable(calendarInfoProvider.getCalendarKey(calendarBucket));
    }
  }

  public long getGlobalPlayTimeTicks() {
    return globalStatistics.getPlayTimeTicks();
  }

  public void incrementPlayTimeTicks(int value, CalendarInfoProvider calendarInfoProvider) {
    globalStatistics.incrementPlayTimeTicks(value);

    for (var calendarBucket : CalendarBucket.ALL_VALUES) {
      var bucketStatistics = statisticsByCalendarBucketOrdinal[calendarBucket.ordinal()];

      bucketStatistics.updateKeyAndResetIfApplicable(calendarInfoProvider.getCalendarKey(calendarBucket));
      bucketStatistics.incrementPlayTimeTicks(value);
    }

    dirty = true;
  }

  public long getGlobalAfkTimeTicks() {
    return globalStatistics.getAfkTimeTicks();
  }

  public void incrementAfkTimeTicks(int value, CalendarInfoProvider calendarInfoProvider) {
    globalStatistics.incrementAfkTimeTicks(value);

    for (var calendarBucket : CalendarBucket.ALL_VALUES) {
      var bucketStatistics = statisticsByCalendarBucketOrdinal[calendarBucket.ordinal()];

      bucketStatistics.updateKeyAndResetIfApplicable(calendarInfoProvider.getCalendarKey(calendarBucket));
      bucketStatistics.incrementAfkTimeTicks(value);
    }

    dirty = true;
  }

  public long getCalendarBucketPlayTimeTicks(CalendarBucket calendarBucket) {
    return statisticsByCalendarBucketOrdinal[calendarBucket.ordinal()].getPlayTimeTicks();
  }

  public long getCalendarBucketAfkTimeTicks(CalendarBucket calendarBucket) {
    return statisticsByCalendarBucketOrdinal[calendarBucket.ordinal()].getAfkTimeTicks();
  }

  public boolean isDirty() {
    return dirty;
  }

  public void clearDirty() {
    this.dirty = false;
  }

  public String serialize() {
    var temporaryConfig = new YamlConfiguration();

    temporaryConfig.set("playTimeTicks", globalStatistics.getPlayTimeTicks());
    temporaryConfig.set("afkTimeTicks", globalStatistics.getAfkTimeTicks());
    temporaryConfig.set("lastKnownName", lastKnownName);

    for (var calendarBucket : CalendarBucket.ALL_VALUES) {
      var bucketSection = temporaryConfig.createSection("calendarBuckets." + calendarBucket.name());
      var bucketStatistics = statisticsByCalendarBucketOrdinal[calendarBucket.ordinal()];

      bucketSection.set("playTimeTicks", bucketStatistics.getPlayTimeTicks());
      bucketSection.set("afkTimeTicks", bucketStatistics.getAfkTimeTicks());
      bucketSection.set("key", bucketStatistics.key);
    }

    return temporaryConfig.saveToString();
  }

  public static UserData makeInitial(UUID playerId, String lastKnownName, CalendarInfoProvider calendarInfoProvider) {
    var statisticsByCalendarBucketOrdinal = new TimeStatisticsAndKey[CalendarBucket.ALL_VALUES.size()];

    for (var calendarBucket : CalendarBucket.ALL_VALUES)
      statisticsByCalendarBucketOrdinal[calendarBucket.ordinal()] = new TimeStatisticsAndKey(0, 0, calendarInfoProvider.getCalendarKey(calendarBucket));

    return new UserData(playerId, lastKnownName, new TimeStatistics(0, 0), statisticsByCalendarBucketOrdinal);
  }

  public static UserData deserialize(UUID playerId, String value) {
    var temporaryConfig = YamlConfiguration.loadConfiguration(new StringReader(value));

    if (!(temporaryConfig.get("playTimeTicks") instanceof Number playTimeTicksNumber))
      throw new IllegalStateException("Missing property \"playTimeTicks\"");

    if (!(temporaryConfig.get("afkTimeTicks") instanceof Number afkTimeTicksNumber))
      throw new IllegalStateException("Missing property \"afkTimeTicks\"");

    if (!(temporaryConfig.get("lastKnownName") instanceof String lastKnownName))
      throw new IllegalStateException("Missing property \"lastKnownName\"");

    var statisticsByCalendarBucketOrdinal = new TimeStatisticsAndKey[CalendarBucket.ALL_VALUES.size()];

    for (var calendarBucket : CalendarBucket.ALL_VALUES) {
      var sectionPath = "calendarBuckets." + calendarBucket.name();
      var bucketSection = temporaryConfig.getConfigurationSection(sectionPath);

      if (bucketSection == null)
        throw new IllegalStateException("Missing section \"" + sectionPath + "\"");

      if (!(bucketSection.get("playTimeTicks") instanceof Number bucketPlayTimeTicksNumber))
        throw new IllegalStateException("Missing property \"" + sectionPath + ".playTimeTicks\"");

      if (!(bucketSection.get("afkTimeTicks") instanceof Number bucketAfkTimeTicksNumber))
        throw new IllegalStateException("Missing property \"" + sectionPath + ".afkTimeTicks\"");

      if (!(bucketSection.get("key") instanceof Number key))
        throw new IllegalStateException("Missing property \"" + sectionPath + ".key\"");

      statisticsByCalendarBucketOrdinal[calendarBucket.ordinal()] = new TimeStatisticsAndKey(
        bucketPlayTimeTicksNumber.longValue(),
        bucketAfkTimeTicksNumber.longValue(),
        key.intValue()
      );
    }

    return new UserData(
      playerId,
      lastKnownName,
      new TimeStatistics(playTimeTicksNumber.longValue(), afkTimeTicksNumber.longValue()),
      statisticsByCalendarBucketOrdinal
    );
  }
}
