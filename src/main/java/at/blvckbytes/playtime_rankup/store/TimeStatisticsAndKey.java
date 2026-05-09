package at.blvckbytes.playtime_rankup.store;

public class TimeStatisticsAndKey extends TimeStatistics {

  public int key;

  public TimeStatisticsAndKey(long playTimeTicks, long afkTimeTicks, int key) {
    super(playTimeTicks, afkTimeTicks);

    this.key = key;
  }

  public boolean updateKeyAndResetIfApplicable(int newKey) {
    if (this.key == newKey)
      return false;

    this.playTimeTicks = 0;
    this.afkTimeTicks = 0;

    return true;
  }
}
