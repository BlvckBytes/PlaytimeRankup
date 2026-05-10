package at.blvckbytes.playtime_rewards.command.top_times;

import at.blvckbytes.component_markup.markup.interpreter.DirectFieldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record TopListPageEntry(int place, String name, long time) implements DirectFieldAccess {

  @Override
  public @Nullable Object accessField(String rawIdentifier) {
    return switch (rawIdentifier) {
      case "place" -> place;
      case "name" -> name;
      case "time" -> time;
      default -> DirectFieldAccess.UNKNOWN_FIELD_SENTINEL;
    };
  }

  @Override
  public Set<String> getAvailableFields() {
    return Set.of("place", "name", "time");
  }
}
