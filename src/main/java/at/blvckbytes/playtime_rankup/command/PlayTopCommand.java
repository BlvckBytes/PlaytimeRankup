package at.blvckbytes.playtime_rankup.command;

import at.blvckbytes.cm_mapper.ConfigKeeper;
import at.blvckbytes.playtime_rankup.config.MainSection;
import at.blvckbytes.playtime_rankup.store.TimeType;
import at.blvckbytes.playtime_rankup.store.UserDataStore;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayTopCommand extends TopCommand {

  public PlayTopCommand(
    UserDataStore userDataStore,
    ConfigKeeper<MainSection> config
  ) {
    super(userDataStore, config, TimeType.PLAY_TIME);
  }

  @Override
  protected void handlePageEntries(@NotNull CommandSender sender, @NotNull String label, List<TopListPageEntry> entries) {
    for (var entry : entries)
      sender.sendMessage("§a#" + entry.place() + " " + entry.name() + " time " + entry.time());
  }
}
