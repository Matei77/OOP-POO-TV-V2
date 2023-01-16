package pages.changePageCommand;

import engine.PlatformActions;
import engine.PlatformEngine;
import pages.Page;
import utils.OutputHandler;

import static utils.Constants.ERROR_STATUS;
import static utils.Constants.SUCCESS_STATUS;

public class ChangePageCommand implements Command {
  private String previousPage;
  @Override
  public int execute() {
    Page currentPage = PlatformEngine.getEngine().getCurrentPage();
    int returnStatus = currentPage.changePage(PlatformActions.getCurrentAction().getPage());
    if (returnStatus == SUCCESS_STATUS) {
      previousPage = currentPage.getPageName();
      // System.out.println("add command " + PlatformActions.getCurrentAction().getPage());
    }
    return returnStatus;
  }

  @Override
  public void undo() {
    if (PlatformEngine.getEngine().getCurrentUser() == null) {
      OutputHandler.updateOutput(ERROR_STATUS);
      // System.out.println("undo error, not logged it");
      return;
    }


    Page currentPage = PlatformEngine.getEngine().getCurrentPage();
    currentPage.changePage(previousPage);
    // System.out.println("going back to " + previousPage);
  }
}
