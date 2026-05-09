package at.blvckbytes.playtime_rankup.store;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;

public enum CalendarBucket {

  DAY(TopListType.DAY) {
    @Override
    public int makeKey(LocalDate currentDate) {
      return currentDate.getYear() * 1000 + currentDate.getDayOfYear();
    }
  },

  WEEK(TopListType.WEEK) {
    @Override
    public int makeKey(LocalDate currentDate) {
      return currentDate.get(WeekFields.ISO.weekBasedYear()) * 100 + currentDate.get(WeekFields.ISO.weekOfWeekBasedYear());
    }
  },

  MONTH(TopListType.MONTH) {
    @Override
    public int makeKey(LocalDate currentDate) {
      return currentDate.getYear() * 100 + currentDate.getMonthValue();
    }
  },

  YEAR(TopListType.YEAR) {
    @Override
    public int makeKey(LocalDate currentDate) {
      return currentDate.getYear();
    }
  },
  ;

  public static final List<CalendarBucket> ALL_VALUES = Arrays.asList(values());

  public final TopListType topListType;

  CalendarBucket(TopListType topListType) {
    this.topListType = topListType;
  }

  public abstract int makeKey(LocalDate currentDate);
}
