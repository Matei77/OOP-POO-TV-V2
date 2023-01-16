package pages.changePageCommand;

public interface Command {
  int execute();
  void undo();
}
