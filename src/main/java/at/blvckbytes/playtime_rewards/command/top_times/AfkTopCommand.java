package at.blvckbytes.playtime_rewards.command.top_times;

import at.blvckbytes.cm_mapper.ConfigKeeper;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.playtime_rewards.config.MainSection;
import at.blvckbytes.playtime_rewards.store.TimeType;
import at.blvckbytes.playtime_rewards.store.TopListType;
import at.blvckbytes.playtime_rewards.store.UserDataStore;
import me.blvckbytes.syllables_matcher.NormalizedConstant;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AfkTopCommand extends TopCommand {

  public AfkTopCommand(
    UserDataStore userDataStore,
    ConfigKeeper<MainSection> config
  ) {
    super(userDataStore, config, TimeType.AFK_TIME);
  }

  @Override
  protected void handlePageEntries(
    @NotNull CommandSender sender, @NotNull String label,
    NormalizedConstant<TopListType> normalizedType, List<TopListPageEntry> entries,
    int currentPage, int numberOfPages, int pageSize
  ) {
    config.rootSection.commands.afkTop.afkTopScreen.sendMessage(
      sender,
      new InterpretationEnvironment()
        .withVariable("top_type", normalizedType.getNormalizedName())
        .withVariable("entries", entries)
        .withVariable("current_page", currentPage)
        .withVariable("number_of_pages", numberOfPages)
        .withVariable("page_size", pageSize)
    );
  }
}
