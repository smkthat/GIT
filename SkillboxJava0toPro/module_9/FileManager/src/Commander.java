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
  public static Command getValidInputCommand(List<Command> commands, String input) {
    if (!input.isEmpty()) {
      input = input.toUpperCase();
      for (Command c : commands) {
        if (c.getName().equals(input)) {
          return c;
        }
      }
    }
    System.err.println("ОШИБКА: -введите существующую команду.");
    return null;
  }

  private List<Command> generateMainMenuCommands() {
    return Arrays.asList(
        new Command("SIZE", "показать размер файла/каталога"),
        new Command("COPY", "скопировать файл/каталог"),
        new Command("EXIT", "выход из программы"));
  }

  private List<Command> generateFileSizeMenuCommands() {
    return Arrays.asList(
        new Command("PATHS", "показать список файлов/каталогов"),
        new Command("FAILED PATHS", "показать список файлов/каталогов без доступа"),
        new Command("INFO", "показать всю информацию"),
        new Command("MAIN MENU", "выйти в главное меню"));
  }

  private List<Command> generateNewFolderCommands() {
    return Arrays.asList(
        new Command("YES", "да"),
        new Command("NO", "нет"),
        new Command("MAIN MENU", "выйти в главное меню"));
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
