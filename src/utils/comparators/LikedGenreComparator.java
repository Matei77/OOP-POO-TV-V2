package utils.comparators;

import data.LikedGenre;

import java.util.Comparator;

/**
 * Comparator for liked genres.
 */
public final class LikedGenreComparator implements Comparator<LikedGenre> {

  @Override
  public int compare(final LikedGenre o1, final LikedGenre o2) {
    if (Integer.compare(o2.getLikes(), o1.getLikes()) == 0) {

      // order lexicographically if the number of likes is the same
      return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());

    } else {
      return Integer.compare(o2.getLikes(), o1.getLikes());
    }
  }
}
