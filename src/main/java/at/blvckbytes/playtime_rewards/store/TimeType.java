package at.blvckbytes.playtime_rewards.store;

import me.blvckbytes.syllables_matcher.EnumMatcher;
import me.blvckbytes.syllables_matcher.MatchableEnum;

import java.util.Arrays;
import java.util.List;

public enum TimeType implements MatchableEnum {
  AFK_TIME,
  PLAY_TIME,
  ;

  public static final List<TimeType> ALL_VALUES = Arrays.asList(values());
  public static final EnumMatcher<TimeType> matcher = new EnumMatcher<>(values());

}
