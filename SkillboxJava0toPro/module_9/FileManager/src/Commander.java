import java.util.*;

public class Commander {
  private List<Command> mainMenuCommands;
  private List<Command> fileSizeMenuCommands;
  private List<Command> newFolderCommands;

  public Commander() {
    mainMenuCommands = generateMainMenuCommands();
    fileSizeMenuCommands = generateFileSizeMenuCommands();
    newFolderCommands = generateNewFolderCommands();
  }

  /** Проверка правильности ввода доступной команды */
  public static Command getValidInputCommand(List<Command> commands, int input) {
    for (Command c : commands) {
      if (c.getId() == input) {
        return c;
      }
    }
    System.err.println("ОШИБКА: -введите цифру соответсвующую команде.");
    return null;
  }

  private List<Command> generateMainMenuCommands() {
    return new ArrayList<>(
        Arrays.asList(
            new Command("GET_SIZE", "показать размер файла/каталога"),
            new Command("COPY_PATH", "скопировать файл/каталог"),
            new Command("EXIT", "выход из программы")));
  }

  private List<Command> generateFileSizeMenuCommands() {
    return new ArrayList<>(
        Arrays.asList(
            new Command("GET_PATH_LIST", "показать список файлов/каталогов"),
            new Command("GET_FAILED_PATH_LIST", "показать список файлов/каталогов без доступа"),
            new Command("GET_ALL_INFO", "показать всю информацию"),
            new Command("MAIN_MENU", "выйти в главное меню")));
  }

  private List<Command> generateNewFolderCommands() {
    return new ArrayList<>(
        Arrays.asList(
            new Command("YES", "да"),
            new Command("NO", "нет"),
            new Command("MAIN_MENU", "выйти в главное меню")));
  }

  public List<Command> getMainMenuCommands() {
    return mainMenuCommands;
  }

  public List<Command> getFileSizeMenuCommands() {
    return fileSizeMenuCommands;
  }

  public List<Command> getConfirmCopyCommands() {
    return newFolderCommands;
  }
}
