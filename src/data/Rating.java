package data;

/**
 * Represents the rating given by a user for a movie.
 */
public final class Rating {
  private User user;
  private Integer rating;

  /**
   * Constructor for movie rating
   * @param user the user that gave the rating
   * @param rating the rating
   */
  public Rating(final User user, final Integer rating) {
    this.user = user;
    this.rating = rating;
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(final Integer rating) {
    this.rating = rating;
  }
}
