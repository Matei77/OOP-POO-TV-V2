package engine;

import data.Movie;
import data.Notification;
import data.User;
import input.MovieInput;
import utils.OutputHandler;
import utils.Utils;

import java.util.ArrayList;

import static utils.Constants.ADD_MESSAGE;
import static utils.Constants.DELETE_MESSAGE;
import static utils.Constants.ERROR_STATUS;


/**
 * Represents the class that contains admin actions. Is implemented using the Observer design
 * pattern. Admin actions are observable by the observers (the users).
 */
public final class AdminActions {
  private static final ArrayList<User> OBSERVERS = new ArrayList<>();

  private AdminActions() { }

  /**
   * Add user to observers list, so they notified when a new admin action is made.
   * @param user user that will be added
   */
  public static void addObserver(final User user) {
    OBSERVERS.add(user);
  }

  /**
   * Remove user from observers list.
   * @param user user that will be removed
   */
  public static void removeObserver(final User user) {
    OBSERVERS.remove(user);
  }


  /**
   * Add new movie to the database and notify users.
   */
  public static void addMovieToDatabase() {
    MovieInput addedMovie = PlatformActions.getCurrentAction().getAddedMovie();

    // check if the movie is already in the database
    if (Utils.findMovieInDatabase(addedMovie.getName()) != null) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    // generate a new movie from input
    Movie movie = new Movie(addedMovie.getName(), addedMovie.getYear(), addedMovie.getDuration(),
        addedMovie.getGenres(), addedMovie.getActors(), addedMovie.getCountriesBanned());

    // add movie to database
    PlatformEngine.getEngine().getMoviesDatabase().add(movie);

    // create notification
    Notification notification = new Notification(movie, ADD_MESSAGE);

    // notify users
    for (User observer : OBSERVERS) {
      observer.notifyObserver(notification);
    }
  }

  /**
   * Delete movie from the database and notify users.
   */
  public static void deleteMovieFromDatabase() {
    String deletedMovie = PlatformActions.getCurrentAction().getDeletedMovie();

    // check if the movie is in the database
    Movie movieToDelete = Utils.findMovieInDatabase(deletedMovie);
    if (movieToDelete == null) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    // remove the movie from the database
    PlatformEngine.getEngine().getMoviesDatabase().remove(movieToDelete);

    // create notification
    Notification notification = new Notification(movieToDelete, DELETE_MESSAGE);

    // notify observers
    for (User observer : OBSERVERS) {
      observer.notifyObserver(notification);
    }
  }
}
