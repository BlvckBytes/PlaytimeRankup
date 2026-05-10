package at.blvckbytes.playtime_rewards.config;

import at.blvckbytes.cm_mapper.mapper.MappingError;
import at.blvckbytes.cm_mapper.mapper.section.CSAlways;
import at.blvckbytes.cm_mapper.mapper.section.CSIgnore;
import at.blvckbytes.cm_mapper.mapper.section.ConfigSection;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.component_markup.util.logging.InterpreterLogger;
import at.blvckbytes.playtime_rewards.rewards_display.config.RewardsDisplaySection;
import at.blvckbytes.playtime_rewards.store.TimeType;
import at.blvckbytes.playtime_rewards.store.TopListType;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.*;

@CSAlways
public class MainSection extends ConfigSection {

  public int saveIntervalSeconds;

  public int topListUpdateIntervalSeconds;

  public int maxTopListSize;

  public int topListCommandsPageSize;

  public int incrementIntervalTicks;

  public int calendarBucketKeyUpdateIntervalSeconds;

  public String timeZoneId;

  public @CSIgnore ZoneId _timeZone;

  public Map<String, RankSection> ranks = new LinkedHashMap<>();
  public @CSIgnore List<RankSection> rankList = new ArrayList<>();

  public CommandsSection commands;

  public CommonMessagesSection commonMessages;

  public TopListTypeSection topListType;
  public TimeTypeSection timeType;

  public RewardsDisplaySection rewardsDisplay;

  public MainSection(InterpretationEnvironment baseEnvironment, InterpreterLogger interpreterLogger) {
    super(baseEnvironment, interpreterLogger);
  }

  @Override
  public void afterParsing(List<Field> fields) throws Exception {
    super.afterParsing(fields);

    if (saveIntervalSeconds <= 0)
      throw new MappingError("Property \"saveIntervalSeconds\" cannot be less than or equal to zero");

    if (topListUpdateIntervalSeconds <= 0)
      throw new MappingError("Property \"topListUpdateIntervalSeconds\" cannot be less than or equal to zero");

    if (maxTopListSize <= 0)
      throw new MappingError("Property \"maxTopListSize\" cannot be less than or equal to zero");

    if (incrementIntervalTicks <= 0)
      throw new MappingError("Property \"incrementIntervalTicks\" cannot be less than or equal to zero");

    if (calendarBucketKeyUpdateIntervalSeconds <= 0)
      throw new MappingError("Property \"calendarBucketKeyUpdateIntervalTicks\" cannot be less than or equal to zero");

    rankList.addAll(ranks.values());

    try {
      _timeZone = ZoneId.of(timeZoneId);
    } catch (Throwable e) {
      throw new MappingError("Property \"timeZoneId\" does not resemble a valid time-zone");
    }

    if (topListType.displayNames != null) {
      for (var topTypeConstant : TopListType.ALL_VALUES) {
        var normalizedConstant = TopListType.matcher.getNormalizedConstant(topTypeConstant);
        var displayName = topListType.displayNames.get(topTypeConstant.name());

        if (displayName == null)
          displayName = normalizedConstant.initialNormalizedName;

        normalizedConstant.setName(displayName);
      }
    }

    if (timeType.displayNames != null) {
      for (var timeTypeConstant : TimeType.ALL_VALUES) {
        var normalizedConstant = TimeType.matcher.getNormalizedConstant(timeTypeConstant);
        var displayName = timeType.displayNames.get(timeTypeConstant.name());

        if (displayName == null)
          displayName = normalizedConstant.initialNormalizedName;

        normalizedConstant.setName(displayName);
      }
    }
  }
}
