package utils.comparators;

import data.Movie;
import engine.PlatformActions;
import input.ActionInput;
import input.SortInput;

import java.util.Comparator;

public final class LikesMovieComparator implements Comparator<Movie> {

  @Override
  public int compare(final Movie o1, final Movie o2) {
      return Integer.compare(o2.getNumLikes(), o1.getNumLikes());
  }
}
