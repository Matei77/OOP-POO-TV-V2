/* Copyright Ionescu Matei-Stefan - 323CAb - 2022-2023 */

package data;

import engine.PlatformEngine;
import utils.OutputHandler;
import utils.comparators.LikedGenreComparator;
import utils.comparators.LikesMovieComparator;

import java.util.ArrayList;

import static utils.Constants.ADD_MESSAGE;
import static utils.Constants.DELETE_MESSAGE;
import static utils.Constants.FREE_MOVIES;
import static utils.Constants.INITIAL_TOKENS;
import static utils.Constants.NO_RECOMMENDATION_MESSAGE;
import static utils.Constants.PREMIUM_ACCOUNT;
import static utils.Constants.RECOMMENDATION_MESSAGE;

/**
 * Represents a user.
 */
public final class User {
  private String name;
  private String password;
  private String accountType;
  private String country;
  private int balance;

  private int numFreePremiumMovies = FREE_MOVIES;
  private int tokensCount = INITIAL_TOKENS;

  private ArrayList<Movie> purchasedMovies;
  private ArrayList<Movie> watchedMovies;
  private ArrayList<Movie> likedMovies;
  private ArrayList<Movie> ratedMovies;
  private ArrayList<Notification> notifications;
  private ArrayList<String> subscribedGenres;

  /**
   * Constructor for new user.
   *
   * @param name name of the user
   * @param password user's password
   * @param accountType the type of account (standard/premium)
   * @param country the country the user is from
   * @param balance the balance of the user
   */
  public User(final String name, final String password, final String accountType,
              final String country, final int balance) {
    this.name = name;
    this.password = password;
    this.accountType = accountType;
    this.country = country;
    this.balance = balance;
    purchasedMovies = new ArrayList<>();
    watchedMovies = new ArrayList<>();
    likedMovies = new ArrayList<>();
    ratedMovies = new ArrayList<>();
    notifications = new ArrayList<>();
    subscribedGenres = new ArrayList<>();
  }

  /**
   * Notify users when a new admin action is made
   * @param notification the notification received from the admin action
   */
  public void notifyObserver(final Notification notification) {
    // check if the notification is about a deleted movie
    if (notification.getMessage().equals(DELETE_MESSAGE)) {
      // check if the user had purchased the movie
      if (this.purchasedMovies.contains(notification.getMovie())) {
        // notify user
        this.notifications.add(notification);

        // reimburse the user
        if (this.accountType.equals(PREMIUM_ACCOUNT)) {
          this.numFreePremiumMovies++;
        } else {
          this.tokensCount += 2;
        }

        // remove the movie from the user's lists
        this.purchasedMovies.remove(notification.getMovie());
        this.watchedMovies.remove(notification.getMovie());
        this.likedMovies.remove(notification.getMovie());
        this.ratedMovies.remove(notification.getMovie());
      }

    // check if the notification is about a new movie
    } else if (notification.getMessage().equals(ADD_MESSAGE)) {
      //check if the movie is banned in this person's country
      if (notification.getMovie().getCountriesBanned().contains(this.country)) {
        return;
      }

      // check if the user is subscribed to one of the genres of the new movie
      boolean hasGenre = false;
      ArrayList<String> addedMovieGenres = notification.getMovie().getGenres();
      for (String genre : addedMovieGenres) {
        if (this.subscribedGenres.contains(genre)) {
          hasGenre = true;
          break;
        }
      }

      // notify the user
      if (hasGenre) {
        this.notifications.add(notification);
      }
    }
  }

  /**
   * Notifies a user about a movie they should watch
   */
  public void giveRecommendation() {
    // initialize the movie to no recommendation
    Movie recommendedMovie = new Movie(NO_RECOMMENDATION_MESSAGE, "", 0, null,
        null, null);

    // add the genres the user liked to an array
    ArrayList<String> genres = new ArrayList<>();
    for (Movie movie : this.likedMovies) {
      genres.addAll(movie.getGenres());
    }

    // create array of the LikedGenre that contains the genre and the number of likes given by
    // the user to movies of that genre
    ArrayList<LikedGenre> likedGenres = new ArrayList<>();
    for (String genre : genres) {
      LikedGenre likedGenre = new LikedGenre(genre);
      likedGenres.add(likedGenre);
    }

    // get the likes given to each genre by the user
    for (Movie movie : this.likedMovies) {
      for (LikedGenre likedGenre : likedGenres) {
        for (String genre : movie.getGenres()) {
          if (genre.equals(likedGenre.getName())) {
            likedGenre.setLikes(likedGenre.getLikes() + 1);
          }
        }
      }
    }

    // sort the likedGenres array
    LikedGenreComparator genreComparator = new LikedGenreComparator();
    likedGenres.sort(genreComparator);

    // get all movies from the database
    ArrayList<Movie> sortedLikedMovies =
        new ArrayList<>(PlatformEngine.getEngine().getMoviesDatabase());
    LikesMovieComparator movieComparator = new LikesMovieComparator();

    // sort the movies according to the number of likes
    sortedLikedMovies.sort(movieComparator);

    // find the movie with the highest number of likes from the user's favorite genre that they
    // haven't watched
    for (LikedGenre genre : likedGenres) {
      for (Movie movie : sortedLikedMovies) {
        if (movie.getGenres().contains(genre.getName()) && !this.watchedMovies.contains(movie)) {
          recommendedMovie = movie;
          break;
        }
      }
    }

    // notify the user about movie
    Notification notification = new Notification(recommendedMovie, RECOMMENDATION_MESSAGE);
    this.notifications.add(notification);

    // update output
    OutputHandler.finalNotification();
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(final String accountType) {
    this.accountType = accountType;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(final int balance) {
    this.balance = balance;
  }

  public int getNumFreePremiumMovies() {
    return numFreePremiumMovies;
  }

  public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
    this.numFreePremiumMovies = numFreePremiumMovies;
  }

  public int getTokensCount() {
    return tokensCount;
  }

  public void setTokensCount(final int tokensCount) {
    this.tokensCount = tokensCount;
  }

  public ArrayList<Movie> getPurchasedMovies() {
    return purchasedMovies;
  }

  public void setPurchasedMovies(final ArrayList<Movie> purchasedMovies) {
    this.purchasedMovies = purchasedMovies;
  }

  public ArrayList<Movie> getWatchedMovies() {
    return watchedMovies;
  }

  public void setWatchedMovies(final ArrayList<Movie> watchedMovies) {
    this.watchedMovies = watchedMovies;
  }

  public ArrayList<Movie> getLikedMovies() {
    return likedMovies;
  }

  public void setLikedMovies(final ArrayList<Movie> likedMovies) {
    this.likedMovies = likedMovies;
  }

  public ArrayList<Movie> getRatedMovies() {
    return ratedMovies;
  }

  public void setRatedMovies(final ArrayList<Movie> ratedMovies) {
    this.ratedMovies = ratedMovies;
  }

  public ArrayList<Notification> getNotifications() {
    return notifications;
  }

  public void setNotifications(final ArrayList<Notification> notifications) {
    this.notifications = notifications;
  }

  public ArrayList<String> getSubscribedGenres() {
    return subscribedGenres;
  }

  public void setSubscribedGenres(final ArrayList<String> subscribedGenres) {
    this.subscribedGenres = subscribedGenres;
  }
}
