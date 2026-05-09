package at.blvckbytes.playtime_rankup.store;

import java.util.Arrays;
import java.util.List;

public enum TimeType {
  AFK_TIME,
  PLAY_TIME,
  ;

  public static final List<TimeType> ALL_VALUES = Arrays.asList(values());
}
