package data;

public class Rating {
  User user;
  Integer rating;

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
