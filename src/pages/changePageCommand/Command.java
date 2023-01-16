package pages.changePageCommand;

public interface Command {
  /**
   * execute the command
   * @return whether the command was executed successfully or not.
   *        ERROR_STATUS (-1) for false;
   *        SUCCESS_STATUS (1) for true;
   */
  int execute();

  /**
   * undo last command executed successfully
   */
  void undo();
}
