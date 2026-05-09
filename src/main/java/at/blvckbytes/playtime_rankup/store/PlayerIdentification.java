package at.blvckbytes.playtime_rankup.store;

import java.util.UUID;

public interface PlayerIdentification {

  UUID getPlayerId();

  String getPlayerName();

}
