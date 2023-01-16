package pages.changePageCommand;

import engine.PlatformActions;
import engine.PlatformEngine;
import pages.Page;
import pages.PageFactory;
import utils.OutputHandler;

import static utils.Constants.*;

public class ChangePageCommand implements Command {
  private String previousPage;
  @Override
  public int execute() {
    Page currentPage = PlatformEngine.getEngine().getCurrentPage();
    int returnStatus = currentPage.changePage(PlatformActions.getCurrentAction().getPage());
    if (returnStatus == SUCCESS_STATUS) {
      previousPage = currentPage.getPageName();
    }
    return returnStatus;
  }

  @Override
  public void undo() {
    if (PlatformEngine.getEngine().getCurrentUser() == null) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }


    Page currentPage = PlatformEngine.getEngine().getCurrentPage();
    if (previousPage.equals(LOGIN_PAGE) || previousPage.equals(REGISTER_PAGE)) {
      PageFactory pageFactory = new PageFactory();
      PlatformEngine.getEngine().setCurrentPage(pageFactory.getPage(LOGGED_IN_HOMEPAGE));

    } else {
      currentPage.changePage(previousPage);
    }
  }
}
