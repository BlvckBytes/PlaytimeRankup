package at.blvckbytes.playtime_rankup.store;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;

public enum CalendarBucket {

  DAY {
    @Override
    public int makeKey(LocalDate currentDate) {
      return currentDate.getYear() * 1000 + currentDate.getDayOfYear();
    }
  },

  WEEK {
    @Override
    public int makeKey(LocalDate currentDate) {
      return currentDate.get(WeekFields.ISO.weekBasedYear()) * 100 + currentDate.get(WeekFields.ISO.weekOfWeekBasedYear());
    }
  },

  MONTH {
    @Override
    public int makeKey(LocalDate currentDate) {
      return currentDate.getYear() * 100 + currentDate.getMonthValue();
    }
  },

  YEAR {
    @Override
    public int makeKey(LocalDate currentDate) {
      return currentDate.getYear();
    }
  },
  ;

  public static final List<CalendarBucket> ALL_VALUES = Arrays.asList(values());

  public abstract int makeKey(LocalDate currentDate);
}
