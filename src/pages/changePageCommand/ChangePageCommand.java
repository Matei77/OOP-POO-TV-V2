package pages.changePageCommand;

import engine.PlatformActions;
import engine.PlatformEngine;
import pages.Page;
import pages.PageFactory;
import utils.OutputHandler;

import static utils.Constants.ERROR_STATUS;
import static utils.Constants.LOGGED_IN_HOMEPAGE;
import static utils.Constants.LOGIN_PAGE;
import static utils.Constants.REGISTER_PAGE;
import static utils.Constants.SUCCESS_STATUS;

public final class ChangePageCommand implements Command {
  private String previousPage;


  @Override
  public int execute() {
    Page currentPage = PlatformEngine.getEngine().getCurrentPage();

    // try to change the page and get the return status
    int returnStatus = currentPage.changePage(PlatformActions.getCurrentAction().getPage());

    // save the previous page if the page change was successful
    if (returnStatus == SUCCESS_STATUS) {
      previousPage = currentPage.getPageName();
    }

    // return whether the action was successful or not
    return returnStatus;
  }

  @Override
  public void undo() {
    // if there is no user logged in the command outputs an error
    if (PlatformEngine.getEngine().getCurrentUser() == null) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    Page currentPage = PlatformEngine.getEngine().getCurrentPage();

    // check if the previous page is login or register, in which case we simply change the page
    // to the logged in homepage without redoing the login/register process
    if (previousPage.equals(LOGIN_PAGE) || previousPage.equals(REGISTER_PAGE)) {
      PageFactory pageFactory = PageFactory.getPageFactory();
      PlatformEngine.getEngine().setCurrentPage(pageFactory.getPage(LOGGED_IN_HOMEPAGE));

    // go to the previous page otherwise
    } else {
      currentPage.changePage(previousPage);
    }
  }
}
