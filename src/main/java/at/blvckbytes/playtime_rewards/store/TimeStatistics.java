package at.blvckbytes.playtime_rewards.store;

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

  public void incrementTime(TimeType timeType, long value) {
    if (value <= 0)
      throw new IllegalArgumentException("Value cannot be less than or equal to zero");

    switch (timeType) {
      case PLAY_TIME -> playTimeTicks += value;
      case AFK_TIME -> afkTimeTicks += value;
    }
  }

  public void decrementTime(TimeType timeType, long value) {
    if (value <= 0)
      throw new IllegalArgumentException("Value cannot be less than or equal to zero");

    switch (timeType) {
      case PLAY_TIME -> playTimeTicks -= Math.min(playTimeTicks, value);
      case AFK_TIME -> afkTimeTicks -= Math.min(afkTimeTicks, value);
    }
  }
}
