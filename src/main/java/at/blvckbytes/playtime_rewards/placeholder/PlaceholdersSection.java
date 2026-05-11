package at.blvckbytes.playtime_rewards.placeholder;

import at.blvckbytes.cm_mapper.cm.ComponentMarkup;
import at.blvckbytes.cm_mapper.mapper.section.ConfigSection;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.component_markup.util.logging.InterpreterLogger;

public class PlaceholdersSection extends ConfigSection {

  public ComponentMarkup timeFormat;
  public ComponentMarkup calendarDay;
  public ComponentMarkup calendarWeek;
  public ComponentMarkup calendarMonth;
  public ComponentMarkup calendarYear;

  public PlaceholdersSection(InterpretationEnvironment baseEnvironment, InterpreterLogger interpreterLogger) {
    super(baseEnvironment, interpreterLogger);
  }
}
