import java.util.*;

public class FileManager {
  private static Command command;
  private static Commander commander;
  private static FileOperator fileOperator;
  private static boolean isRunning = true;

  public static void main(String[] args) {
    System.out.println("\n" + Helper.getTab(6) + "CONSOLE FILE BROWSER v.2");
    startMainMenu();
  }

  /** Показывает меню доступных команд */
  private static void showMenu(List<Command> commands) {
    StringBuilder builder = new StringBuilder();
    builder
        .append("------------------------------------------------------------------------------\n")
        .append(Helper.getTab(0))
        .append("Доступные команды:\n");

    for (Command c : commands) {
      builder
          .append(Helper.getTab(6))
          .append(c.getName())
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
    commander = new Commander();
    fileOperator = new FileOperator();

    while (isRunning) {
      List<Command> availableCommands = commander.getMainMenuCommands();
      command = null;
      String input = "";
      do {
        showMenu(availableCommands);
        System.out.println("Введите команду: ");
        try {
          input = new Scanner(System.in).nextLine();
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
        case "SIZE":
          {
            fileOperator.sizeInfo();
            showFileSizeActionMenu();
            startMainMenu();
            break;
          }
        case "COPY":
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

  /** Показывает меню подтверждения копирования */
  public static boolean showConfirmCopyActionMenu() {
    List<Command> availableCommands = commander.getConfirmCopyCommands();
    String input = "";
    command = null;
    do {
      showMenu(availableCommands);
      System.out.println("Введите команду: ");
      try {
        input = new Scanner(System.in).nextLine();
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
      case "MAIN MENU":
        {
          startMainMenu();
          break;
        }
    }

    return false;
  }

  /** Показывает меню доп.действий */
  private static void showFileSizeActionMenu() {
    List<Command> availableCommands = commander.getFileSizeMenuCommands();
    String input = "";
    command = null;
    do {
      showMenu(availableCommands);
      System.out.println("Введите команду: ");
      try {
        input = new Scanner(System.in).nextLine();
      } catch (InputMismatchException ignored) {
      }
      command = Commander.getValidInputCommand(availableCommands, input);
    } while (command == null);

    /*----------------------------------------------------------------*/

    switch (command.getName()) {
      case "PATHS":
        {
          fileOperator.showPaths();
          showFileSizeActionMenu();
          break;
        }
      case "FAILED PATHS":
        {
          fileOperator.showFailedPaths();
          showFileSizeActionMenu();
          break;
        }
      case "INFO":
        {
          System.out.println(fileOperator.getInfo());
          showFileSizeActionMenu();
          break;
        }
      case "MAIN MENU":
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
