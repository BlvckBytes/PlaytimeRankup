package at.blvckbytes.playtime_rankup.store;

import me.blvckbytes.syllables_matcher.EnumMatcher;
import me.blvckbytes.syllables_matcher.MatchableEnum;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public enum TopListType implements MatchableEnum {

  GLOBAL(null) {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getGlobalTimeTicks(timeType);
    }
  },

  DAY(CalendarBucket.DAY) {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getCalendarBucketTimeTicks(CalendarBucket.DAY, timeType);
    }
  },

  WEEK(CalendarBucket.WEEK) {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getCalendarBucketTimeTicks(CalendarBucket.WEEK, timeType);
    }
  },

  MONTH(CalendarBucket.MONTH) {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getCalendarBucketTimeTicks(CalendarBucket.MONTH, timeType);
    }
  },

  YEAR(CalendarBucket.YEAR) {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getCalendarBucketTimeTicks(CalendarBucket.YEAR, timeType);
    }
  },
  ;

  public static final List<TopListType> ALL_VALUES = Arrays.asList(values());
  public static final EnumMatcher<TopListType> matcher = new EnumMatcher<>(values());

  public final @Nullable CalendarBucket calendarBucket;

  TopListType(@Nullable CalendarBucket calendarBucket) {
    this.calendarBucket = calendarBucket;
  }

  public abstract long accessStatistic(UserData userData, TimeType timeType);
}
