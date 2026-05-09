package at.blvckbytes.playtime_rankup.rankup;

import at.blvckbytes.playtime_rankup.store.PlayerIdentification;
import at.blvckbytes.playtime_rankup.store.UserData;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerAndMeta implements PlayerIdentification {

  public final Player player;
  public final com.earth2me.essentials.User essentialsUser;
  public final net.luckperms.api.model.user.User luckPermsUser;
  public final UserData userData;

  public final Set<String> claimedRanks = new HashSet<>();

  public PlayerAndMeta(
    Player player,
    com.earth2me.essentials.User essentialsUser,
    net.luckperms.api.model.user.User luckPermsUser,
    UserData userData
  ) {
    this.player = player;
    this.essentialsUser = essentialsUser;
    this.luckPermsUser = luckPermsUser;
    this.userData = userData;
  }

  @Override
  public UUID getPlayerId() {
    return player.getUniqueId();
  }

  @Override
  public String getPlayerName() {
    return player.getName();
  }
}
