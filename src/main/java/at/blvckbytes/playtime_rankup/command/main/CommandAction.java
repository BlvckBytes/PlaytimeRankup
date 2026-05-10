package at.blvckbytes.playtime_rankup.command.main;

import me.blvckbytes.syllables_matcher.EnumMatcher;
import me.blvckbytes.syllables_matcher.MatchableEnum;

public enum CommandAction implements MatchableEnum {
  RELOAD,
  ADD_PLAY_TIME,
  SUBTRACT_PLAY_TIME,
  ADD_AFK_TIME,
  SUBTRACT_AFK_TIME,
  ;

  public static final EnumMatcher<CommandAction> matcher = new EnumMatcher<>(values());
}
