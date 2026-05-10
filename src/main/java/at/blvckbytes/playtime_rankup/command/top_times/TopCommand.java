package at.blvckbytes.playtime_rankup.command.top_times;

import at.blvckbytes.cm_mapper.ConfigKeeper;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.playtime_rankup.config.MainSection;
import at.blvckbytes.playtime_rankup.store.TimeType;
import at.blvckbytes.playtime_rankup.store.TopListType;
import at.blvckbytes.playtime_rankup.store.UserDataStore;
import me.blvckbytes.syllables_matcher.NormalizedConstant;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class TopCommand implements CommandExecutor, TabCompleter {

  private final UserDataStore userDataStore;
  protected final ConfigKeeper<MainSection> config;
  private final TimeType timeType;

  public TopCommand(
    UserDataStore userDataStore,
    ConfigKeeper<MainSection> config,
    TimeType timeType
  ) {
    this.userDataStore = userDataStore;
    this.config = config;
    this.timeType = timeType;
  }

  protected abstract void handlePageEntries(
    @NotNull CommandSender sender, @NotNull String label,
    NormalizedConstant<TopListType> normalizedType, List<TopListPageEntry> entries,
    int currentPage, int numberOfPages, int pageSize
  );

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (args.length > 2) {
      config.rootSection.commonMessages.topCommandUsage.sendMessage(
        sender,
        new InterpretationEnvironment()
          .withVariable("label", label)
          .withVariable("types", TopListType.matcher.createCompletions(null))
      );

      return true;
    }

    var normalizedType = TopListType.matcher.getNormalizedConstant(TopListType.GLOBAL);

    if (args.length > 0) {
      normalizedType = TopListType.matcher.matchFirst(args[0]);

      if (normalizedType == null) {
        config.rootSection.commonMessages.topCommandUsage.sendMessage(
          sender,
          new InterpretationEnvironment()
            .withVariable("label", label)
            .withVariable("types", TopListType.matcher.createCompletions(null))
        );

        return true;
      }
    }

    var page = 1;

    if (args.length == 2) {
      try {
        page = Integer.parseInt(args[1]);

        if (page < 0)
          throw new IllegalStateException();
      } catch (Throwable e) {
        config.rootSection.commonMessages.topCommandInvalidPage.sendMessage(
          sender,
          new InterpretationEnvironment()
            .withVariable("input", args[1])
        );

        return true;
      }
    }

    var topList = userDataStore.getTopList(normalizedType.constant, timeType);

    if (topList.isEmpty()) {
      config.rootSection.commonMessages.topCommandEmptyTopList.sendMessage(sender);
      return true;
    }

    var pageSize = config.rootSection.topListCommandsPageSize;
    var numberOfPages = (topList.size() + pageSize - 1) / pageSize;

    if (page > numberOfPages) {
      config.rootSection.commonMessages.topCommandExceededPages.sendMessage(
        sender,
        new InterpretationEnvironment()
          .withVariable("page", page)
          .withVariable("number_of_pages", numberOfPages)
      );

      return true;
    }

    var pageEntries = new ArrayList<TopListPageEntry>();
    var firstIndex = (page - 1) * pageSize;

    for (var index = firstIndex; index < firstIndex + pageSize; ++index) {
      if (index >= topList.size())
        break;

      var userData = topList.get(index);

      pageEntries.add(new TopListPageEntry(
        index + 1,
        userData.getLastKnownName(),
        normalizedType.constant.accessStatistic(userData, timeType))
      );
    }

    handlePageEntries(sender, label, normalizedType, pageEntries, page, numberOfPages, pageSize);
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (args.length == 1)
      return TopListType.matcher.createCompletions(args[0]);

    if (args.length == 2) {
      var normalizedType = TopListType.matcher.matchFirst(args[0]);

      if (normalizedType == null)
        return List.of();

      var topList = userDataStore.getTopList(normalizedType.constant, timeType);
      var pageSize = config.rootSection.topListCommandsPageSize;
      var numberOfPages = (topList.size() + pageSize - 1) / pageSize;

      return IntStream.range(1, numberOfPages + 1)
        .mapToObj(String::valueOf)
        .filter(it -> it.startsWith(args[1]))
        .limit(15)
        .toList();
    }

    return List.of();
  }
}
