package at.blvckbytes.playtime_rewards.config;

import at.blvckbytes.cm_mapper.mapper.section.ConfigSection;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.component_markup.util.logging.InterpreterLogger;

import java.util.HashMap;
import java.util.Map;

public class TimeTypeSection extends ConfigSection {

  public Map<String, String> displayNames = new HashMap<>();

  public TimeTypeSection(InterpretationEnvironment baseEnvironment, InterpreterLogger interpreterLogger) {
    super(baseEnvironment, interpreterLogger);
  }
}
