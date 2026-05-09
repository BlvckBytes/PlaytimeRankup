package at.blvckbytes.playtime_rankup.command;

import at.blvckbytes.playtime_rankup.store.TimeType;
import at.blvckbytes.playtime_rankup.store.TopListType;
import at.blvckbytes.playtime_rankup.store.UserDataStore;
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

  // TODO: Add to config
  private static final int PAGE_SIZE = 10;

  private final UserDataStore userDataStore;
  private final TimeType timeType;

  public TopCommand(UserDataStore userDataStore, TimeType timeType) {
    this.userDataStore = userDataStore;
    this.timeType = timeType;
  }

  protected abstract void handlePageEntries(@NotNull CommandSender sender, @NotNull String label, List<TopListPageEntry> entries);

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (args.length == 0 || args.length > 2) {
      sender.sendMessage("§cUsage: /" + label + " <type> [page]");
      return true;
    }

    var normalizedType = TopListType.matcher.matchFirst(args[0]);

    if (normalizedType == null) {
      sender.sendMessage("§cUnknown type!");
      return true;
    }

    var page = 1;

    if (args.length == 2) {
      try {
        page = Integer.parseInt(args[1]);

        if (page < 0)
          throw new IllegalStateException();
      } catch (Throwable e) {
        sender.sendMessage("§cInvalid page-value: " + args[1]);
        return true;
      }
    }

    var topList = userDataStore.getTopList(normalizedType.constant, timeType);

    if (topList.isEmpty()) {
      sender.sendMessage("§cThere are no entries in the top-list yet!");
      return true;
    }

    var numberOfPages = (topList.size() + PAGE_SIZE - 1) / PAGE_SIZE;

    if (page > numberOfPages) {
      sender.sendMessage("§cThere's no page " + page + ", seeing how there are only " + numberOfPages + " pages in total!");
      return true;
    }

    var pageEntries = new ArrayList<TopListPageEntry>();
    var firstIndex = (page - 1) * PAGE_SIZE;

    for (var index = firstIndex; index < firstIndex + PAGE_SIZE; ++index) {
      if (index >= topList.size())
        break;

      var userData = topList.get(index);

      pageEntries.add(new TopListPageEntry(
        index + 1,
        userData.getLastKnownName(),
        normalizedType.constant.accessStatistic(userData, timeType))
      );
    }

    handlePageEntries(sender, label, pageEntries);
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
      var numberOfPages = (topList.size() + PAGE_SIZE - 1) / PAGE_SIZE;

      return IntStream.range(1, numberOfPages + 1)
        .mapToObj(String::valueOf)
        .filter(it -> it.startsWith(args[1]))
        .limit(15)
        .toList();
    }

    return List.of();
  }
}
