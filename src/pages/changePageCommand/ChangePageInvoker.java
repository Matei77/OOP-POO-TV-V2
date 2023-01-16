package pages.changePageCommand;

import engine.PlatformEngine;
import utils.OutputHandler;

import java.util.LinkedList;

import static utils.Constants.ERROR_STATUS;
import static utils.Constants.SUCCESS_STATUS;

public class ChangePageInvoker {
  private LinkedList<Command> history = new LinkedList<>();

  public ChangePageInvoker() { }

  public void restart() {
    history.clear();
  }

  public void execute(Command command) {
    int returnStatus = command.execute();
    if (returnStatus == SUCCESS_STATUS && PlatformEngine.getEngine().getCurrentUser() != null) {
      history.push(command);
    }
  }

  public void undo() {
    if (history.isEmpty()) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    Command command = history.pop();

    if (command != null) {
      command.undo();
    }
  }
}
