package utils.comparators;

import data.Movie;

import java.util.Comparator;

/**
 * Comparator for the number of likes of a movie
 */
public final class LikesMovieComparator implements Comparator<Movie> {

  @Override
  public int compare(final Movie o1, final Movie o2) {
      return Integer.compare(o2.getNumLikes(), o1.getNumLikes());
  }
}
