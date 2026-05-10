package at.blvckbytes.playtime_rankup.command.top_times;

import at.blvckbytes.cm_mapper.cm.ComponentMarkup;
import at.blvckbytes.cm_mapper.section.command.CommandSection;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.component_markup.util.logging.InterpreterLogger;

public class AfkTopCommandSection extends CommandSection {

  public static final String INITIAL_NAME = "afktop";

  public ComponentMarkup afkTopScreen;

  public AfkTopCommandSection(InterpretationEnvironment baseEnvironment, InterpreterLogger interpreterLogger) {
    super(INITIAL_NAME, baseEnvironment, interpreterLogger);
  }
}
