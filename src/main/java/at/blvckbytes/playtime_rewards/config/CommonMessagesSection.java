package at.blvckbytes.playtime_rewards.config;

import at.blvckbytes.cm_mapper.cm.ComponentMarkup;
import at.blvckbytes.cm_mapper.mapper.section.ConfigSection;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.component_markup.util.logging.InterpreterLogger;

public class CommonMessagesSection extends ConfigSection {

  public ComponentMarkup onlyAvailableToPlayers;
  public ComponentMarkup hasNotPlayedBefore;
  public ComponentMarkup topCommandUsage;
  public ComponentMarkup topCommandInvalidPage;
  public ComponentMarkup topCommandEmptyTopList;
  public ComponentMarkup topCommandExceededPages;
  public ComponentMarkup topScreen;

  public CommonMessagesSection(InterpretationEnvironment baseEnvironment, InterpreterLogger interpreterLogger) {
    super(baseEnvironment, interpreterLogger);
  }
}
