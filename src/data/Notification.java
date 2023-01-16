package data;

/**
 * Represents a notification received by the user.
 */
public final class Notification {
  private Movie movie;
  private String message;

  /**
   * Constructor for notification
   * @param movie the movie that the notification is related to
   * @param message the message of the notification
   */
  public Notification(final Movie movie, final String message) {
    this.movie = movie;
    this.message = message;
  }

  public Movie getMovie() {
    return movie;
  }

  public void setMovie(final Movie movie) {
    this.movie = movie;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
