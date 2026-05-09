package at.blvckbytes.playtime_rankup.store;

public class TimeStatistics {

  protected long playTimeTicks;
  protected long afkTimeTicks;

  public TimeStatistics(long playTimeTicks, long afkTimeTicks) {
    this.playTimeTicks = playTimeTicks;
    this.afkTimeTicks = afkTimeTicks;

    if (playTimeTicks < 0)
      throw new IllegalStateException("Property \"playTimeTicks\" is negative");

    if (afkTimeTicks < 0)
      throw new IllegalStateException("Property \"afkTimeTicks\" is negative");
  }

  public long getTime(TimeType timeType) {
    return switch (timeType) {
      case PLAY_TIME -> playTimeTicks;
      case AFK_TIME -> afkTimeTicks;
    };
  }

  public void incrementPlayTimeTicks(int value) {
    if (value <= 0)
      throw new IllegalArgumentException("Value cannot be less than or equal to zero!");

    playTimeTicks += value;
  }

  public void incrementAfkTimeTicks(int value) {
    if (value <= 0)
      throw new IllegalArgumentException("Value cannot be less than or equal to zero!");

    afkTimeTicks += value;
  }
}
