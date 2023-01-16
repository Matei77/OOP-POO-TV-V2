package data;

import static utils.Constants.INITIAL_LIKES;

/**
 * Represents a genre that a user likes and stores the name of the genre and the number of likes
 * the user gave to movies from this specific genre.
 */
public final class LikedGenre {
  private String name;
  private int likes = INITIAL_LIKES;

  public LikedGenre(final String name) {
    this.setName(name);
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(final int likes) {
    this.likes = likes;
  }
}
