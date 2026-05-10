package at.blvckbytes.playtime_rewards.command.top_times;

import at.blvckbytes.cm_mapper.ConfigKeeper;
import at.blvckbytes.playtime_rewards.config.MainSection;
import at.blvckbytes.playtime_rewards.store.TimeType;
import at.blvckbytes.playtime_rewards.store.UserDataStore;

public class AfkTopCommand extends TopCommand {

  public AfkTopCommand(
    UserDataStore userDataStore,
    ConfigKeeper<MainSection> config
  ) {
    super(userDataStore, config, TimeType.AFK_TIME);
  }
}
