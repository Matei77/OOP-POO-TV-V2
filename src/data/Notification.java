package data;

public class Notification {
  private Movie movie;
  private String message;

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
