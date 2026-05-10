package at.blvckbytes.playtime_rewards.store;

public class TimeStatisticsAndKey extends TimeStatistics {

  public int key;

  public TimeStatisticsAndKey(long playTimeTicks, long afkTimeTicks, int key) {
    super(playTimeTicks, afkTimeTicks);

    this.key = key;
  }

  public boolean resetIfApplicable() {
    if (playTimeTicks == 0 && afkTimeTicks == 0)
      return false;

    playTimeTicks = 0;
    afkTimeTicks = 0;

    return true;
  }

  public boolean updateKeyAndResetIfApplicable(int newKey) {
    if (key == newKey)
      return false;

    key = newKey;

    return resetIfApplicable();
  }
}
