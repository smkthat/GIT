
import java.util.*;

public class FileManager {
  private static Command command;
  private static Commander commander;
  private static FileOperator fileOperator;
  private static boolean isRunning = true;
  private static final int ZERO_STATE = 0;

  public static void main(String[] args) {
    System.out.println("\n" + FileOperator.getTab(6) + "CONSOLE FILE BROWSER v.2");
    commander = new Commander();
    startMainMenu();
  }

  /** Показывает меню доступных команд */
  private static void showMenu(List<Command> commands) {
    StringBuilder builder = new StringBuilder();
    builder
        .append("------------------------------------------------------------------------------\n")
        .append(FileOperator.getTab(0))
        .append("Доступные команды:\n");

    for (Command c : commands) {
      builder
          .append(FileOperator.getTab(6))
          .append(c.getId())
          .append("\t- ")
          .append(c.getComment())
          .append("\n");
    }
    builder.append(
        "------------------------------------------------------------------------------\n");
    System.out.println(builder.toString());
  }

  /** Старт программы с главного меню */
  public static void startMainMenu() {
    fileOperator = new FileOperator();

    while (isRunning) {
      List<Command> availableCommands = commander.getMainMenuCommands();
      command = null;
      int input = ZERO_STATE;
      do {
        showMenu(availableCommands);
        System.out.println("Введите команду: ");
        try {
          input = new Scanner(System.in).nextInt();
        } catch (InputMismatchException ignored) {
        }
        command = Commander.getValidInputCommand(availableCommands, input);
      } while (command == null);

      /*----------------------------------------------------------------*/

      switch (command.getName()) {
        case "EXIT":
          {
            System.out.println("Хорошего дня ;)");
            stopProgram();
            break;
          }
        case "GET_SIZE":
          {
            fileOperator.sizeInfo();
            showFileSizeActionMenu();
            startMainMenu();
            break;
          }
        case "COPY_PATH":
          {
            fileOperator.startCopy();
            startMainMenu();
            break;
          }
        default:
          {
            System.err.println("ОШИБКА: -что-то пошло не так, неизвестная команда! :(");
            startMainMenu();
            break;
          }
      }
    }
  }

  public static boolean showConfirmCopyActionMenu() {
    List<Command> availableCommands = commander.getConfirmCopyCommands();
    int input = ZERO_STATE;
    command = null;
    do {
      showMenu(availableCommands);
      System.out.println("Введите команду: ");
      try {
        input = new Scanner(System.in).nextInt();
      } catch (InputMismatchException ignored) {
      }
      command = Commander.getValidInputCommand(availableCommands, input);
    } while (command == null);

    /*----------------------------------------------------------------*/

    switch (command.getName()) {
      case "NO":
        {
          fileOperator.startCopy();
          break;
        }
      case "YES":
        {
          System.out.println("Начинаем копирование!\n");
          return true;
        }
      case "MAIN_MENU":
        {
          startMainMenu();
          break;
        }
    }

    return false;
  }

  private static void showFileSizeActionMenu() {
    List<Command> availableCommands = commander.getFileSizeMenuCommands();
    int input = ZERO_STATE;
    command = null;
    do {
      showMenu(availableCommands);
      System.out.println("Введите команду: ");
      try {
        input = new Scanner(System.in).nextInt();
      } catch (InputMismatchException ignored) {
      }
      command = Commander.getValidInputCommand(availableCommands, input);
    } while (command == null);

    /*----------------------------------------------------------------*/

    switch (command.getName()) {
      case "GET_PATH_LIST":
        {
          System.out.println(fileOperator.pathListToString(fileOperator.getFiles()));
          showFileSizeActionMenu();
          break;
        }
      case "GET_FAILED_PATH_LIST":
        {
          System.out.println(fileOperator.pathListToString(fileOperator.getAccessDeniedFiles()));
          showFileSizeActionMenu();
          break;
        }
      case "GET_ALL_INFO":
        {
          System.out.println(fileOperator.getInfo());
          showFileSizeActionMenu();
          break;
        }
      case "MAIN_MENU":
        {
          startMainMenu();
          break;
        }
    }
  }

  /** Завершение программы */
  private static void stopProgram() {
    isRunning = false;
  }
}
