package at.blvckbytes.playtime_rewards.store;

import me.blvckbytes.syllables_matcher.EnumMatcher;
import me.blvckbytes.syllables_matcher.MatchableEnum;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public enum TopListDirection implements MatchableEnum {

  DESCENDING {
    @Override
    public void sort(List<UserData> list, TopListType topListType, TimeType timeType) {
      list.sort(Comparator.<UserData>comparingLong(a -> topListType.accessStatistic(a, timeType)).reversed());
    }
  },

  ASCENDING {
    @Override
    public void sort(List<UserData> list, TopListType topListType, TimeType timeType) {
      list.sort(Comparator.comparingLong(a -> topListType.accessStatistic(a, timeType)));
    }
  },
  ;

  public static final List<TopListDirection> ALL_VALUES = Arrays.asList(values());
  public static final EnumMatcher<TopListDirection> matcher = new EnumMatcher<>(values());

  public abstract void sort(List<UserData> list, TopListType topListType, TimeType timeType);
}
