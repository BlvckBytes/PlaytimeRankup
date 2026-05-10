package at.blvckbytes.playtime_rankup.store;

import me.blvckbytes.syllables_matcher.EnumMatcher;
import me.blvckbytes.syllables_matcher.MatchableEnum;

import java.util.Arrays;
import java.util.List;

public enum TopListType implements MatchableEnum {

  GLOBAL {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getGlobalTimeTicks(timeType);
    }
  },

  DAY {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getCalendarBucketTimeTicks(CalendarBucket.DAY, timeType);
    }
  },

  WEEK {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getCalendarBucketTimeTicks(CalendarBucket.WEEK, timeType);
    }
  },

  MONTH {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getCalendarBucketTimeTicks(CalendarBucket.MONTH, timeType);
    }
  },

  YEAR {
    @Override
    public long accessStatistic(UserData userData, TimeType timeType) {
      return userData.getCalendarBucketTimeTicks(CalendarBucket.YEAR, timeType);
    }
  },
  ;

  public static final List<TopListType> ALL_VALUES = Arrays.asList(values());
  public static final EnumMatcher<TopListType> matcher = new EnumMatcher<>(values());

  public abstract long accessStatistic(UserData userData, TimeType timeType);

  public CalendarBucket getCalendarBucket() {
    return switch (this) {
      case GLOBAL -> null;
      case DAY -> CalendarBucket.DAY;
      case WEEK -> CalendarBucket.WEEK;
      case MONTH -> CalendarBucket.MONTH;
      case YEAR -> CalendarBucket.YEAR;
    };
  }
}
