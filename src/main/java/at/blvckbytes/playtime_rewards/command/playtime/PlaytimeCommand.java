package at.blvckbytes.playtime_rewards.command.playtime;

import at.blvckbytes.cm_mapper.ConfigKeeper;
import at.blvckbytes.component_markup.expression.interpreter.InterpretationEnvironment;
import at.blvckbytes.playtime_rewards.command.OfflinePlayerRegistry;
import at.blvckbytes.playtime_rewards.config.MainSection;
import at.blvckbytes.playtime_rewards.store.UserDataStore;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlaytimeCommand implements CommandExecutor, TabCompleter {

  private final UserDataStore userDataStore;
  private final OfflinePlayerRegistry offlinePlayerRegistry;
  private final ConfigKeeper<MainSection> config;

  public PlaytimeCommand(
    UserDataStore userDataStore,
    OfflinePlayerRegistry offlinePlayerRegistry,
    ConfigKeeper<MainSection> config
  ) {
    this.userDataStore = userDataStore;
    this.offlinePlayerRegistry = offlinePlayerRegistry;
    this.config = config;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    OfflinePlayer target;

    if (args.length == 0) {
      if (!(sender instanceof Player player)) {
        config.rootSection.commands.playtime.consoleExecutionWithoutName.sendMessage(sender);
        return true;
      }

      target = player;
    }

    else {
      if (args.length != 1) {
        config.rootSection.commands.playtime.commandUsage.sendMessage(
          sender,
          new InterpretationEnvironment()
            .withVariable("label", label)
        );
        return true;
      }

      target = offlinePlayerRegistry.getPlayerByName(args[0]);

      if (target == null) {
        config.rootSection.commonMessages.hasNotPlayedBefore.sendMessage(
          sender,
          new InterpretationEnvironment()
            .withVariable("name", args[0])
        );

        return true;
      }
    }

    var data = userDataStore.access(target);

    if (target == sender) {
      config.rootSection.commands.playtime.playtimeOfSelf.sendMessage(sender, data.makeEnvironment());
      return true;
    }

    config.rootSection.commands.playtime.playtimeOfOther.sendMessage(sender, data.makeEnvironment());
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      var typedNameLower = args[0].toLowerCase();

      return offlinePlayerRegistry.streamKnownNames()
        .filter(it -> it.toLowerCase().startsWith(typedNameLower))
        .limit(15)
        .toList();
    }

    return List.of();
  }
}
