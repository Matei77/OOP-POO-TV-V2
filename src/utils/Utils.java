/* Copyright Ionescu Matei-Stefan - 323CAb - 2022-2023 */

package utils;

import engine.AdminActions;
import engine.PlatformEngine;
import input.Input;
import input.MovieInput;
import input.UserInput;
import data.Movie;
import data.User;
import pages.changePageCommand.ChangePageInvoker;
import pages.pageTypes.LoggedOutHomepage;

import java.util.ArrayList;

public final class Utils {
  private Utils() { }

  /**
   * Set the starting state of the engine.
   */
  public static void setStartingState() {
    ArrayList<Movie> currentMoviesList = new ArrayList<>();
    PlatformEngine.getEngine().setCurrentMoviesList(currentMoviesList);
    PlatformEngine.getEngine().setCurrentPage(new LoggedOutHomepage());
    PlatformEngine.getEngine().setCurrentUser(null);

    ChangePageInvoker changePageInvoker = new ChangePageInvoker();
    PlatformEngine.getEngine().setChangePageInvoker(changePageInvoker);
  }

  /**
   * Set the databases for users and movies in the engine.
   */
  public static void setDatabases() {
    Input inputData = PlatformEngine.getEngine().getInputData();

    // create users database
    ArrayList<User> usersDatabase = new ArrayList<>();
    ArrayList<UserInput> inputUsers = inputData.getUsers();
    for (UserInput inputUser : inputUsers) {
      User user = new User(inputUser.getCredentials().getName(),
          inputUser.getCredentials().getPassword(), inputUser.getCredentials().getAccountType(),
          inputUser.getCredentials().getCountry(), inputUser.getCredentials().getBalance());
      usersDatabase.add(user);
      AdminActions.addObserver(user);
    }

    // create movies database
    ArrayList<Movie> moviesDatabase = new ArrayList<>();
    ArrayList<MovieInput> inputMovies = inputData.getMovies();
    for (MovieInput inputMovie : inputMovies) {
      Movie movie = new Movie(inputMovie.getName(), inputMovie.getYear(), inputMovie.getDuration(),
          inputMovie.getGenres(), inputMovie.getActors(), inputMovie.getCountriesBanned());
      moviesDatabase.add(movie);
    }

    // add databases to engine
    PlatformEngine.getEngine().setUsersDatabase(usersDatabase);
    PlatformEngine.getEngine().setMoviesDatabase(moviesDatabase);
  }

  /**
   * Search a movie by name in the list of movies the user is seen on the screen.
   *
   * @param movieName the name of the movie searched
   * @return the Movie instance from the movies list if found or null otherwise
   */
  public static Movie findMovie(final String movieName) {
    ArrayList<Movie> currentMoviesList = PlatformEngine.getEngine().getCurrentMoviesList();

    for (Movie movie : currentMoviesList) {
      if (movie.getName().equals(movieName)) {
        return movie;
      }
    }

    return null;
  }

  /**
   * Search a movie by name in the database.
   *
   * @param movieName the name of the movie searched
   * @return the Movie instance from the database if found or null otherwise
   */
  public static Movie findMovieInDatabase(final String movieName) {
    ArrayList<Movie> moviesDatabase = PlatformEngine.getEngine().getMoviesDatabase();

    for (Movie movie : moviesDatabase) {
      if (movie.getName().equals(movieName)) {
        return movie;
      }
    }

    return null;
  }
  /**
   * Search a user by name in the database.
   *
   * @param userName the name of the user searched
   * @return the User instance from the database if found or null otherwise
   */
  public static User findUser(final String userName) {
    ArrayList<User> usersDatabase = PlatformEngine.getEngine().getUsersDatabase();

    for (User user : usersDatabase) {
      if (user.getName().equals(userName)) {
        return user;
      }
    }

    return null;
  }
}
