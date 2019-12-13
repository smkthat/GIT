import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileBrowser {
  private static int command;

  /** Константы команд */
  private static final int EXIT = 0;

  private static final int ROOT_PATH = 1;
  private static final int COPY_PATH = 2;
  private static final int OVERWRITE = 3;
  private static final int SAVE_BOTH = 4;
  private static final int EXECUTE_NEW_COPY = 5;
  private static final int CHANGE_COPY_PATH = 6;
  private static final int MAIN_MENU = 7;
  private static final int NO = 8;
  private static final int YES = 9;
  private static final int RENAME = 10;
  private static final int DELETE = 99;
  private static final int ZERO_STATE = -1;

  private static boolean copyFinished;
  private static boolean isRunning;

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("\n" + getTab(7) + "CONSOLE FILE BROWSER v.2");
    startMainMenu();
  }

  /** Запуск программы */
  private static void startMainMenu() throws IOException, InterruptedException {
    isRunning = true;
    while (isRunning) {
      command = ZERO_STATE;
      int[] availableCommands = new int[] {EXIT, ROOT_PATH, COPY_PATH, RENAME, DELETE};
      String input;
      copyFinished = false;

      do {
        System.out.println(
            "------------------------------------------------------------------------------");
        System.out.println(
            "Доступные команды:   "
                + ROOT_PATH
                + " - показать древо вложенных файлов и папок;\n"
                + "                     "
                + COPY_PATH
                + " - скопировать файл или папку по заданному пути;\n"
                + "                     "
                + RENAME
                + " - переименовать файл или папку;\n\n"
                + "                     "
                + DELETE
                + " - удаление файла/каталога;\n"
                + "                     "
                + EXIT
                + " - остановка программы.");
        System.out.println(
            "------------------------------------------------------------------------------");

        System.out.println("Введите команду: ");
        input = new Scanner(System.in).nextLine();
      } while (isInvalidInputCommand(availableCommands, input));

      switch (command) {
        case EXIT:
          {
            System.out.println("Хорошего дня ;)");
            stopProgram();
          }
        case ROOT_PATH:
          {
            String path = validInputExistPath("\nВведите путь к папке\\файлу:");
            System.out.println("\nРезультат от " + path);
            showTreeOrCopy(path, 0, null);
            break;
          }
        case COPY_PATH:
          {
            do {
              String path = validInputExistPath("\nВведите путь к копируемой папке\\файлу:");
              String copyPath = validInputNoExistPath("\nВведите путь для копирования:");
              showTreeOrCopy(path, 0, copyPath);
            } while (!copyFinished);
            System.out.println("\nКопирование выполнено!");
            break;
          }
        case RENAME:
          {
            String path;
            String name;
            do {
              path = validInputExistPath("\nВведите путь к папке\\файлу:");
              name = getInputName();
            } while (!rename(new File(path), name));
            System.out.println("\nПереименование выполнено!");
            break;
          }
        case DELETE:
          {
            String path;
            do {
              path = validInputExistPath("\nВведите путь к папке\\файлу для удаления:");
            } while (!delete(new File(path)));
            System.out.println("\nУдаление выполнено!");
            break;
          }
        default:
          {
            stopProgram();
          }
      }
    }
  }

  /** Переименование файла или каталога */
  private static boolean rename(File file, String name) {
    String renamedPath;
    if (file.isFile()) {
      renamedPath = file.getParent() + "\\" + name;
    } else if (file.isDirectory()) {
      renamedPath = file.getPath().substring(0, file.getPath().lastIndexOf("\\") + 1) + name;
    } else {
      return false;
    }

    return file.renameTo(new File(renamedPath));
  }

  /** Удаляет файл или дерикторию со всеми вложениями */
  private static boolean delete(File file) {
    try {
      Files.walkFileTree(
          Paths.get(file.getPath()),
          new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
                throws IOException {
              Files.delete(file);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                throws IOException {
              Files.delete(dir);
              return FileVisitResult.CONTINUE;
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /** Получение пути к существующему файлу/каталогу */
  private static String validInputExistPath(String message) {
    String path;
    boolean isValidPath;
    do {
      System.out.println(message);
      path = new Scanner(System.in).nextLine();
      isValidPath = new File(path).exists();
      if (!isValidPath) {
        System.err.println("ОШИБКА: -некорректный путь.");
      }
    } while (!isValidPath);
    return path;
  }

  /** Создание нового каталога и получение валидного пути к нему */
  private static String validInputNoExistPath(String message)
      throws IOException, InterruptedException {
    String path;
    boolean isExistsPath;
    do {
      System.out.println(message);
      path = new Scanner(System.in).nextLine();
      isExistsPath = new File(path).exists();

      if (!isExistsPath) {
        int[] availableCommands = new int[] {EXIT, NO, YES, MAIN_MENU};
        String input;
        do {
          System.out.println(
              "Каталог \""
                  + path
                  + "\" не существует. Хотите создать?\n"
                  + "------------------------------------------------------------------------------");
          System.out.println(
              "Доступные команды:   "
                  + NO
                  + " - нет;\n"
                  + "                     "
                  + YES
                  + " - да;\n"
                  + "                     "
                  + MAIN_MENU
                  + " - вернуться в главное меню;\n\n"
                  + "                     "
                  + EXIT
                  + " - остановка программы.");
          System.out.println(
              "------------------------------------------------------------------------------");

          System.out.println("Введите команду: ");
          input = new Scanner(System.in).nextLine();
        } while (isInvalidInputCommand(availableCommands, input));

        switch (command) {
          case NO:
            {
              break;
            }
          case YES:
            {
              System.out.println("Каталог \"" + path + "\" создан.");
              isExistsPath = new File(path).mkdir();
              break;
            }
          case MAIN_MENU:
            {
              startMainMenu();
            }
          case EXIT:
            {
              stopProgram();
            }
          default:
            {
              break;
            }
        }
      }

      if (!isExistsPath) {
        System.err.println("ОШИБКА: -некорректный путь.");
      }

    } while (!isExistsPath);
    return path;
  }

  /** Проверка правильности ввода доступной команды */
  private static boolean isInvalidInputCommand(int[] commands, String input) {
    try {
      command = Integer.parseInt(input);
      for (int value : commands) {
        if (command == value) {
          return false;
        }
      }
    } catch (NumberFormatException e) {
      System.err.println("ОШИБКА: -введите цифру соответсвующую команде.");
      command = ZERO_STATE;
    }
    return true;
  }

  /** Проверка пути копирования */
  private static boolean isValidCopyPath(File file, File copyFile) {
    if (file == null || copyFile == null) {
      System.err.println(
          "ОШИБКА: -невозможно скопировать файл или папку."
              + "\nПожалуйста, введите другой путь источника или копирования.");
      return false;
    }

    if (file.getPath().equals(copyFile.getPath()) || file.getParent().equals(copyFile.getPath())) {
      System.err.println(
          "ОШИБКА: -совпадение источника с копированием, невозможно перезаписать файл."
              + "\nПожалуйста, введите другой путь источника или копирования.");
      return false;
    }

    return true;
  }

  /** Проверка на совпадение имен файлов и выполнение действий над ними */
  private static void overwriteCopyCheck(int dirLevel, File file, File copyFile)
      throws IOException, InterruptedException {
    if (file.isFile()) {
      if (copyFile.exists()) {
        int overwriteCommand = ZERO_STATE;
        while (overwriteCommand != MAIN_MENU) {
          // Получаем команду перезаписи
          overwriteCommand = startOverwriteFileCommand();

          switch (overwriteCommand) {
            case OVERWRITE:
              {
                getFileInfoOrCopy(file, copyFile);
                overwriteCommand = MAIN_MENU;
                break;
              }
            case EXECUTE_NEW_COPY:
              {
                break;
              }
            case SAVE_BOTH:
              {
                String name = getInputName();
                String path = copyFile.getParent() + "\\" + name;
                copyFile = new File(path);
                overwriteCopyCheck(dirLevel, file, copyFile);
                overwriteCommand = MAIN_MENU;
                break;
              }
            case CHANGE_COPY_PATH:
              {
                for (; ; ) {
                  copyFile = changeCopyFilePath(file);
                  if (isValidCopyPath(file, copyFile)) {
                    getFileInfoOrCopy(file, copyFile);
                    break;
                  }
                }
              }
            case MAIN_MENU:
              {
                overwriteCommand = MAIN_MENU;
                break;
              }
            case EXIT:
              {
                stopProgram();
              }
            default:
              {
                break;
              }
          }
        }
      } else {
        getFileInfoOrCopy(file, copyFile);
      }

    } else if (file.isDirectory()) {
      copyFile = new File(copyFile.getParent());
      System.out.println("Дата записи:" + getTab(4) + "Путь и размер файла:");
      System.out.println(
          "------------------------------------------------------------------------------");
      getDirRoot(file, dirLevel, copyFile);

    } else if (!file.isFile() && !file.isDirectory()) {
      System.err.println("ОШИБКА: -неверно указан путь к файлу или папке.");
    }
  }

  /** Получаем новое разрешенное имя файла */
  private static String getInputName() {
    String input;
    do {
      System.out.println("Введите новое имя файла: ");
      input = new Scanner(System.in).nextLine();
    } while (!isValidName(input));
    return input;
  }

  /** Запускает процесс копирования либо показа содержимого файла или каталога */
  private static void showTreeOrCopy(final String path, final int dirLevel, final String copyPath)
      throws IOException, InterruptedException {
    File file = new File(path);

    if (copyPath == null) {
      if (file.isFile()) {
        getFileInfoOrCopy(file, null);
      } else if (file.isDirectory()) {
        getDirRoot(file, dirLevel, null);
      } else if (!file.isFile() && !file.isDirectory()) {
        System.err.println("ОШИБКА: -неверно указан путь к файлу или папке.");
      }

    } else {
      File copyFile = new File(copyPath + "\\" + file.getName());
      // Проверка совпадения путей
      if (!isValidCopyPath(file, copyFile)) {
        return;
      }

      System.out.println("\nКопируем \t" + path + "\n       в\t" + copyPath);
      overwriteCopyCheck(dirLevel, file, copyFile);
    }
  }

  /** Меняет путь для копирования */
  private static File changeCopyFilePath(File file) throws IOException, InterruptedException {
    while (true) {
      String copyPath = validInputNoExistPath("\nВведите путь копирования в:");
      File copyFile = new File(copyPath);
      String filePath = file.getParent();

      if (copyFile.isDirectory() && !copyPath.equals(filePath)) {
        System.out.println("\nКопируем \t" + file + "\n" + "       в\t" + copyPath);
        overwriteCopyCheck(0, file, copyFile);

        return copyFile;
      } else {
        System.out.println("ОШИБКА: -неверно указан путь копирования.");
      }
    }
  }

  /** Запускает меню перезаписи файла */
  private static int startOverwriteFileCommand() {
    int[] availableCommands =
        new int[] {EXIT, OVERWRITE, SAVE_BOTH, EXECUTE_NEW_COPY, CHANGE_COPY_PATH, MAIN_MENU};
    String input;
    do {
      System.out.println(
          "------------------------------------------------------------------------------");
      System.out.println("Файл с таким именем уже существует!\n");
      System.out.println(
          "Доступные команды:   "
              + OVERWRITE
              + " - перезаписать;\n"
              + "                     "
              + SAVE_BOTH
              + " - сохранить оба файла;\n"
              + "                     "
              + EXECUTE_NEW_COPY
              + " - выполнить другое копирование;\n"
              + "                     "
              + CHANGE_COPY_PATH
              + " - ввести другой путь для копирования файла;\n"
              + "                     "
              + MAIN_MENU
              + " - вернуться в главное меню;\n\n"
              + "                     "
              + EXIT
              + " - выход.");
      System.out.println(
          "------------------------------------------------------------------------------");
      System.out.println("Введите команду: ");
      input = new Scanner(System.in).nextLine();

    } while (isInvalidInputCommand(availableCommands, input));

    return command;
  }

  /** Получаем файлы директории и выполняем операции */
  private static void getDirRoot(File file, int dirLevel, File copyFile)
      throws IOException, InterruptedException {
    if (copyFile != null) {
      // Создаем копируемый каталог
      String rootDir = file.getName();
      rootDir = rootDir.substring(rootDir.lastIndexOf("\\") + 1);
      String newCopyPath = copyFile + "\\" + rootDir;

      copyFile = new File(newCopyPath);

      copyFolder(file, copyFile);
    } else {
      // Печатает каталоги и подкаталоги и присваивает им уровень глубины,
      // если File является каталогом/подкаталогом,
      // иначе печататет информацию о File
      File[] files = file.listFiles();
      if (files != null) {
        for (File dirFile : files) {
          if (dirFile.isDirectory()) {
            System.out.println(getTab(dirLevel) + dirFile.getName() + "\\");
            showTreeOrCopy(dirFile.getPath(), dirLevel + 1, null);
          } else if (dirFile.isFile()) {
            System.out.println(
                getTab(dirLevel)
                    + dirFile.getName()
                    + "\t"
                    + "- "
                    + getReadableSize(dirFile.length()));
          }
        }
      }
    }
  }

  /** Информация или копирование файла */
  private static void getFileInfoOrCopy(File file, File copyFile) {
    // Печатает данные о File и делает его копию
    if (copyFile == null) {
      System.out.println("\nИнформация о файле:");
    } else {
      System.out.println("\nКопируем файл:");
    }

    System.out.println(
        "имя = "
            + file.getName()
            + "\n"
            + "размер = "
            + getReadableSize(file.length())
            + "\n"
            + "создан = "
            + getFormattedDate(new Date(file.lastModified()))
            + "\n"
            + "из = "
            + file.getParent()
            + "в:");

    if (copyFile != null) {
      copyFile(file, copyFile);
    }
  }

  /** Копирует директорию со всеми вложенными папками и файлами */
  private static void copyFolder(File target, File copy) throws InterruptedException {
    // Если копируемый файл является каталогом
    if (target.isDirectory()) {
      // Проверяет наличие данного каталога/подкаталога,
      // если он не существует - создаем
      if (copy.mkdirs()) {
        System.out.println(getFormattedDate(new Date()) + getTab(2) + copy.getPath());
      }

      // Получаем список всех вложенных файлов и папок
      String[] files = target.list();

      if (files != null) {
        for (String file : files) {
          // Перебор и копирование файлов
          File targetFile = new File(target, file);
          File copyFile = new File(copy, file);

          TimeUnit.MILLISECONDS.sleep(40);
          copyFolder(targetFile, copyFile);
        }
      }

    } else {
      // Если копируемый файл является файлом
      // производим его запись
      copyFile(target, copy);
    }
  }

  /** Запись файла */
  private static void copyFile(File target, File copy) {
    InputStream in = null;
    OutputStream out = null;

    try {
      in = new FileInputStream(target);
      out = new FileOutputStream(copy);

      byte[] buffer = new byte[1024];

      int length;
      while ((length = in.read(buffer)) > 0) {
        out.write(buffer, 0, length);
      }

      System.out.println(
          getFormattedDate(new Date())
              + getTab(2)
              + copy.getPath()
              + " - "
              + getReadableSize(copy.length()));
    } catch (Exception e) {
      try {
        Objects.requireNonNull(in).close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      try {
        Objects.requireNonNull(out).close();
      } catch (IOException e2) {
        e2.printStackTrace();
      }
    }

    copyFinished = true;
  }

  /** Создаем отступы в зависимости от уровня папки в которой находится File */
  private static String getTab(int dirLevel) {
    return "\t".repeat(Math.max(0, dirLevel));
  }

  /** Преобразует текущую дату в необходимый формат */
  private static String getFormattedDate(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
    return format.format(date);
  }

  /** Переводит размер файлов к читабельному виду */
  private static String getReadableSize(long size) {
    String[] units = new String[] {"B", "KB", "MB", "GB", "TB"};
    if (size == 0) {
      return "0 " + units[0];
    }

    int unitIndex = (int) (Math.log10(size) / 3);
    double unitValue = 1 << (unitIndex * 10);

    return new DecimalFormat("#,##0.##").format(size / unitValue) + " " + units[unitIndex];
  }

  /** Проверяем имя на недопустимые символы для среды Windows */
  public static boolean isValidName(String name) {
    Pattern pattern =
        Pattern.compile(
            "# Match a valid Windows filename (unspecified file system).          \n"
                + "^                                # Anchor to start of string.        \n"
                + "(?!                              # Assert filename is not: CON, PRN, \n"
                + "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n"
                + "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n"
                + "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n"
                + "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n"
                + "  (?:\\.[^.]*)?                  # followed by optional extension    \n"
                + "  $                              # and end of string                 \n"
                + ")                                # End negative lookahead assertion. \n"
                + "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n"
                + "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n"
                + "$                                # Anchor to end of string.            ",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
    Matcher matcher = pattern.matcher(name);
    return matcher.matches();
  }

  /** Завершение программы */
  private static void stopProgram() {
    isRunning = false;
  }
}
