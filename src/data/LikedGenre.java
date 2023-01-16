package data;

import java.util.ArrayList;

import static utils.Constants.INITIAL_LIKES;

public class LikedGenre {
  private String name;
  private int likes = INITIAL_LIKES;

  public LikedGenre(final String name) {
    this.setName(name);
  }

  public boolean isIn(ArrayList<LikedGenre> obj) {
    for (LikedGenre likedGenre : obj) {
      if (this.getName().equals(likedGenre.getName())) {
        return true;
      }
    }
    return false;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }
}
