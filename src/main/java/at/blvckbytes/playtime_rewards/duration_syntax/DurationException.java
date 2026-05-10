package at.blvckbytes.playtime_rewards.duration_syntax;

public class DurationException extends Exception {

  public final String input;

  public final DurationError error;
  public final Object errorToken;

  public DurationException(String input, DurationError error, Object errorToken) {
    this.input = input;
    this.error = error;
    this.errorToken = errorToken;
  }
}
