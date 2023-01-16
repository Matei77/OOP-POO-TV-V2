/* Copyright Ionescu Matei-Stefan - 323CAb - 2022-2023 */

package pages.pageTypes;

import data.Movie;
import data.Rating;
import data.User;
import engine.PlatformActions;
import engine.PlatformEngine;
import input.ActionInput;
import pages.PageFactory;
import utils.OutputHandler;

import java.util.ArrayList;

import static utils.Constants.ERROR_STATUS;
import static utils.Constants.LOGGED_IN_HOMEPAGE;
import static utils.Constants.LOGOUT_PAGE;
import static utils.Constants.MAX_RATING;
import static utils.Constants.MIN_RATING;
import static utils.Constants.MOVIES_PAGE;
import static utils.Constants.MOVIE_PRICE;
import static utils.Constants.PREMIUM_ACCOUNT;
import static utils.Constants.SEE_DETAILS_PAGE;
import static utils.Constants.SUCCESS_STATUS;
import static utils.Constants.UPGRADES_PAGE;

/**
 * Represents the page the user sees when he selects a movie.
 */
public final class SeeDetailsPage extends LoggedInHomepage {
  @Override
  public int changePage(final String nextPage) {

    // change page according to its type
    if (nextPage.equals(LOGGED_IN_HOMEPAGE)) {
      PageFactory pageFactory = new PageFactory();
      PlatformEngine.getEngine().setCurrentPage(pageFactory.getPage(LOGGED_IN_HOMEPAGE));
      return SUCCESS_STATUS;
    }

    if (nextPage.equals(UPGRADES_PAGE)) {
      PageFactory pageFactory = new PageFactory();
      PlatformEngine.getEngine().setCurrentPage(pageFactory.getPage(UPGRADES_PAGE));
      return SUCCESS_STATUS;
    }

    if (nextPage.equals(MOVIES_PAGE)) {
      gotoMovies();
      return SUCCESS_STATUS;
    }

    if (nextPage.equals(LOGOUT_PAGE)) {
      logout();
      return SUCCESS_STATUS;
    }

    if (nextPage.equals(SEE_DETAILS_PAGE)) {
      return SUCCESS_STATUS;
    }

    // output in case of error
    OutputHandler.updateOutput(ERROR_STATUS);
    return ERROR_STATUS;
  }

  @Override
  public String getPageName() {
    return SEE_DETAILS_PAGE;
  }

  @Override
  public void purchase() {
    // get the user and the movie the user wants to purchase
    User currentUser = PlatformEngine.getEngine().getCurrentUser();
    Movie selectedMovie = PlatformEngine.getEngine().getCurrentMoviesList().get(0);

    // check if the user has already bought the movie
    if (currentUser.getPurchasedMovies().contains(selectedMovie)) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    // purchase the movie according to the users account type
    // if user is premium decrease the number of free movies available
    // otherwise the users pays with tokens
    if (currentUser.getAccountType().equals(PREMIUM_ACCOUNT)) {
      int numFreePremMovies = currentUser.getNumFreePremiumMovies();
      if (numFreePremMovies > 0) {
        numFreePremMovies--;
        currentUser.setNumFreePremiumMovies(numFreePremMovies);
      } else {
        if (currentUser.getTokensCount() < MOVIE_PRICE) {
          OutputHandler.updateOutput(ERROR_STATUS);
          return;
        }
        currentUser.setTokensCount(currentUser.getTokensCount() - MOVIE_PRICE);
      }
    } else {
      if (currentUser.getTokensCount() < MOVIE_PRICE) {
        OutputHandler.updateOutput(ERROR_STATUS);
        return;
      }
      currentUser.setTokensCount(currentUser.getTokensCount() - MOVIE_PRICE);
    }

    // add the movie to the user's list of purchased movies
    ArrayList<Movie> purchasedMovies = currentUser.getPurchasedMovies();
    purchasedMovies.add(selectedMovie);
    currentUser.setPurchasedMovies(purchasedMovies);

    // output the results
    OutputHandler.updateOutput(SUCCESS_STATUS);
  }

  @Override
  public void watch() {
    // get the selected movie and user
    Movie selectedMovie = PlatformEngine.getEngine().getCurrentMoviesList().get(0);
    User currentUser = PlatformEngine.getEngine().getCurrentUser();

    // check if the user purchased the movie first
    if (!currentUser.getPurchasedMovies().contains(selectedMovie)) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    // check if the user has already watched the movie and add it to the watched movies if this is
    // not the case
    if (!currentUser.getWatchedMovies().contains(selectedMovie)) {
      ArrayList<Movie> watchedMovies = currentUser.getWatchedMovies();
      watchedMovies.add(selectedMovie);
      currentUser.setWatchedMovies(watchedMovies);
    }

    // output the results
    OutputHandler.updateOutput(SUCCESS_STATUS);
  }

  @Override
  public void like() {
    // get the selected movie and user
    Movie selectedMovie = PlatformEngine.getEngine().getCurrentMoviesList().get(0);
    User currentUser = PlatformEngine.getEngine().getCurrentUser();

    // check if the user has purchased and watched the movie first
    if (!currentUser.getPurchasedMovies().contains(selectedMovie)
        || !currentUser.getWatchedMovies().contains(selectedMovie)) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    // update the user's liked movies array and the movie's number of likes
    if (!currentUser.getLikedMovies().contains(selectedMovie)) {
      ArrayList<Movie> likedMovies = currentUser.getLikedMovies();

      likedMovies.add(selectedMovie);
      currentUser.setLikedMovies(likedMovies);

      selectedMovie.setNumLikes(selectedMovie.getNumLikes() + 1);
    }

    // output the results
    OutputHandler.updateOutput(SUCCESS_STATUS);
  }

  @Override
  public void rate() {
    ActionInput currentAction = PlatformActions.getCurrentAction();

    // get the selected movie and user
    User currentUser = PlatformEngine.getEngine().getCurrentUser();
    Movie selectedMovie = PlatformEngine.getEngine().getCurrentMoviesList().get(0);

    // check if the user has purchased and watched the movie first
    if (!currentUser.getPurchasedMovies().contains(selectedMovie)
        || !currentUser.getWatchedMovies().contains(selectedMovie)) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    // check if the rating is valid
    if (currentAction.getRate() > MAX_RATING || currentAction.getRate() < MIN_RATING) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    // update the user's rated movies array and the movie's rating
    if (!currentUser.getRatedMovies().contains(selectedMovie)) {
      ArrayList<Movie> ratedMovies = currentUser.getRatedMovies();
      ratedMovies.add(selectedMovie);
      currentUser.setRatedMovies(ratedMovies);
      Rating newRating = new Rating(currentUser, currentAction.getRate());
      selectedMovie.updateRating(newRating);

    } else {
      ArrayList<Rating> ratings = selectedMovie.getRatings();
      for (Rating rating : ratings) {
        if (rating.getUser().equals(currentUser)) {
          rating.setRating(currentAction.getRate());
          break;
        }
      }
      selectedMovie.recalculateRating();
    }

    // output the results
    OutputHandler.updateOutput(SUCCESS_STATUS);
  }

  @Override
  public void subscribe() {
    ActionInput currentAction = PlatformActions.getCurrentAction();

    // get the user and the movie the user is seeing
    User currentUser = PlatformEngine.getEngine().getCurrentUser();
    Movie selectedMovie = PlatformEngine.getEngine().getCurrentMoviesList().get(0);

    String subscribedGenre = currentAction.getSubscribedGenre();

    if (selectedMovie.getGenres().contains(subscribedGenre)) {
      if (!currentUser.getSubscribedGenres().contains(subscribedGenre)) {
        currentUser.getSubscribedGenres().add(subscribedGenre);
      } else {
        OutputHandler.updateOutput(ERROR_STATUS);
      }
    } else {
      OutputHandler.updateOutput(ERROR_STATUS);
    }
  }
}
