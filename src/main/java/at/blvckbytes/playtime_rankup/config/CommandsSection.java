package at.blvckbytes.playtime_rankup.config;

import at.blvckbytes.cm_mapper.mapper.section.CSAlways;
import at.blvckbytes.cm_mapper.mapper.section.ConfigSection;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.component_markup.util.logging.InterpreterLogger;
import at.blvckbytes.playtime_rankup.command.main.MainCommandSection;
import at.blvckbytes.playtime_rankup.command.playtime.PlaytimeCommandSection;
import at.blvckbytes.playtime_rankup.command.rewards.RewardsCommandSection;
import at.blvckbytes.playtime_rankup.command.top_times.AfkTopCommandSection;
import at.blvckbytes.playtime_rankup.command.top_times.PlayTopCommandSection;

@CSAlways
public class CommandsSection extends ConfigSection {

  public PlayTopCommandSection playTop;
  public AfkTopCommandSection afkTop;
  public PlaytimeCommandSection playtime;
  public RewardsCommandSection rewards;
  public MainCommandSection main;

  public CommandsSection(InterpretationEnvironment baseEnvironment, InterpreterLogger interpreterLogger) {
    super(baseEnvironment, interpreterLogger);
  }
}
