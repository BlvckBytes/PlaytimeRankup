package at.blvckbytes.playtime_rankup.command.playtime;

import at.blvckbytes.cm_mapper.cm.ComponentMarkup;
import at.blvckbytes.cm_mapper.section.command.CommandSection;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.component_markup.util.logging.InterpreterLogger;

public class PlaytimeCommandSection extends CommandSection {

  public static final String INITIAL_NAME = "playtime";

  public ComponentMarkup consoleExecutionWithoutName;
  public ComponentMarkup commandUsage;
  public ComponentMarkup playtimeOfSelf;
  public ComponentMarkup playtimeOfOther;

  public PlaytimeCommandSection(InterpretationEnvironment baseEnvironment, InterpreterLogger interpreterLogger) {
    super(INITIAL_NAME, baseEnvironment, interpreterLogger);
  }
}
