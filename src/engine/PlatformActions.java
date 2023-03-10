/* Copyright Ionescu Matei-Stefan - 323CAb - 2022-2023 */

package engine;

import input.ActionInput;
import pages.Page;
import pages.changePageCommand.ChangePageCommand;
import pages.changePageCommand.Command;

import java.util.ArrayList;

import static utils.Constants.ADD_FEATURE;
import static utils.Constants.BACK_TYPE;
import static utils.Constants.BUY_PREMIUM_ACCOUNT_FEATURE;
import static utils.Constants.BUY_TOKENS_FEATURE;
import static utils.Constants.CHANGE_PAGE;
import static utils.Constants.DATABASE_TYPE;
import static utils.Constants.DELETE_FEATURE;
import static utils.Constants.FILTER_FEATURE;
import static utils.Constants.LIKE_FEATURE;
import static utils.Constants.LOGIN_FEATURE;
import static utils.Constants.ON_PAGE;
import static utils.Constants.PREMIUM_ACCOUNT;
import static utils.Constants.PURCHASE_FEATURE;
import static utils.Constants.RATE_FEATURE;
import static utils.Constants.REGISTER_FEATURE;
import static utils.Constants.SEARCH_FEATURE;
import static utils.Constants.SUBSCRIBE_FEATURE;
import static utils.Constants.WATCH_FEATURE;

/**
 * Implements the execution of actions that can be performed on the platform.
 */
public final class PlatformActions {

  private static ActionInput currentAction;

  private PlatformActions() { }

  /**
   * Executes the actions from the actions ArrayList.
   *
   * @param actions array list of the actions that need to be performed
   */
  public static void executeActions(final ArrayList<ActionInput> actions) {
    for (ActionInput action : actions) {
      currentAction = action;
      Page currentPage = PlatformEngine.getEngine().getCurrentPage();

      String type = action.getType();
      if (type.equals(CHANGE_PAGE)) {
        // Call the invoker to execute the new change page command
        Command changePageCommand = new ChangePageCommand();
        PlatformEngine.getEngine().getChangePageInvoker().execute(changePageCommand);

      } else if (type.equals(ON_PAGE)) {
        String feature = action.getFeature();

        switch (feature) {
          case LOGIN_FEATURE -> currentPage.login();
          case REGISTER_FEATURE -> currentPage.register();
          case SEARCH_FEATURE -> currentPage.search();
          case FILTER_FEATURE -> currentPage.filter();
          case BUY_TOKENS_FEATURE -> currentPage.buyTokens();
          case BUY_PREMIUM_ACCOUNT_FEATURE -> currentPage.buyPremium();
          case PURCHASE_FEATURE -> currentPage.purchase();
          case WATCH_FEATURE -> currentPage.watch();
          case LIKE_FEATURE -> currentPage.like();
          case RATE_FEATURE -> currentPage.rate();
          case SUBSCRIBE_FEATURE -> currentPage.subscribe();
          default -> { }
        }
      } else if (type.equals(DATABASE_TYPE)) {
        String feature = action.getFeature();

        switch (feature) {
          case ADD_FEATURE -> AdminActions.addMovieToDatabase();
          case DELETE_FEATURE -> AdminActions.deleteMovieFromDatabase();
          default -> { }
        }
      } else if (type.equals(BACK_TYPE)) {
        PlatformEngine.getEngine().getChangePageInvoker().undo();
      }
    }

    // check if there is a logged-in user and if their account is premium to give them a
    // recommendation
    if (PlatformEngine.getEngine().getCurrentUser() != null) {
      if (PlatformEngine.getEngine().getCurrentUser().getAccountType().equals(PREMIUM_ACCOUNT)) {
        PlatformEngine.getEngine().getCurrentUser().giveRecommendation();
      }
    }
  }

  public static ActionInput getCurrentAction() {
    return currentAction;
  }
}
