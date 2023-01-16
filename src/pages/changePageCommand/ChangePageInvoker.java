package pages.changePageCommand;

import engine.PlatformEngine;
import utils.OutputHandler;

import java.util.LinkedList;

import static utils.Constants.ERROR_STATUS;
import static utils.Constants.SUCCESS_STATUS;

/**
 * Invoker for the change page command.
 */
public class ChangePageInvoker {
  private final LinkedList<Command> history = new LinkedList<>();

  public ChangePageInvoker() { }


  /**
   * Reset the history of commands.
   */
  public void restart() {
    history.clear();
  }

  /**
   * Execute the given command
   * @param command the command that should be executed
   */
  public void execute(final Command command) {
    // execute the command and get the return status
    int returnStatus = command.execute();

    // check if the command happened successfully and add it to the history of commands if this
    // is the case
    if (returnStatus == SUCCESS_STATUS && PlatformEngine.getEngine().getCurrentUser() != null) {
      history.push(command);
    }
  }

  /**
   * Undo the last command
   */
  public void undo() {
    // output an error if there is no command to undo
    if (history.isEmpty()) {
      OutputHandler.updateOutput(ERROR_STATUS);
      return;
    }

    // undo the command
    Command command = history.pop();
    if (command != null) {
      command.undo();
    }
  }
}
