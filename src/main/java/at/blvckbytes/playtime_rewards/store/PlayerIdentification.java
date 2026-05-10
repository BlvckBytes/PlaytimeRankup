package at.blvckbytes.playtime_rewards.store;

import java.util.UUID;

public interface PlayerIdentification {

  UUID getPlayerId();

  String getPlayerName();

}
