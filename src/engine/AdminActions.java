package engine;

import data.Movie;
import data.Notification;
import data.User;
import input.MovieInput;
import utils.OutputHandler;
import utils.Utils;

import java.util.ArrayList;

import static utils.Constants.*;


public final class AdminActions {
  private static ArrayList<User> observers = new ArrayList<>();

  private AdminActions() { }

  public static void addObserver(User user) {
    observers.add(user);
  }

  public void removeObserver(User user) {
    observers.remove(user);
  }

  public static void addMovieToDatabase() {
    MovieInput addedMovie = PlatformActions.getCurrentAction().getAddedMovie();
    if (Utils.findMovie(addedMovie.getName()) != null) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    Movie movie = new Movie(addedMovie.getName(), addedMovie.getYear(), addedMovie.getDuration(),
        addedMovie.getGenres(), addedMovie.getActors(), addedMovie.getCountriesBanned());

    PlatformEngine.getEngine().getMoviesDatabase().add(movie);

    Notification notification = new Notification(movie, ADD_MESSAGE);


    for (User observer : observers) {
      observer.notifyObserver(notification);
    }
  }

  public static void deleteMovieFromDatabase() {
    String deletedMovie = PlatformActions.getCurrentAction().getDeletedMovie();

    Movie movieToDelete = Utils.findMovieInDatabase(deletedMovie);
    if (movieToDelete == null) {
      // System.out.println("did not found " + deletedMovie);
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    PlatformEngine.getEngine().getMoviesDatabase().remove(movieToDelete);

    Notification notification = new Notification(movieToDelete, DELETE_MESSAGE);

    for (User observer : observers) {
      observer.notifyObserver(notification);
    }
  }
}
