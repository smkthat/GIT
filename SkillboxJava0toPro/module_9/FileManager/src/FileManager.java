import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FileManager {
  private static int command;

  /** Константы команд */
  private static final int EXIT = 0;

  private static final int GET_SIZE = 1;
  private static final int COPY_PATH = 2;
  private static final int NO = 3;
  private static final int YES = 4;
  private static final int GET_PATH_LIST = 4;
  private static final int GET_FAILED_PATH_LIST = 5;
  private static final int GET_ALL_INFO = 6;
  private static final int MAIN_MENU = 7;
  private static final int ZERO_STATE = -1;

  private static boolean isRunning = true;
  private static FileSizeCounter sizeCounter;

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("\n" + getTab(7) + "CONSOLE FILE BROWSER v.2");
    startMainMenu();
  }

  /** Запуск программы */
  private static void startMainMenu() throws IOException, InterruptedException {
    while (isRunning) {
      command = ZERO_STATE;
      int[] availableCommands = new int[] {EXIT, GET_SIZE, COPY_PATH};
      String input;

      do {
        System.out.println(
            "------------------------------------------------------------------------------");
        System.out.println(
                "Доступные команды:   "
                        + GET_SIZE
                        + " - показать размер файла или каталога;\n"
                        + "                     "
                        + COPY_PATH
                        + " - скопировать файл или каталог;\n\n"
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
            break;
          }
        case GET_SIZE:
          {
            Path path;
            sizeCounter = null;
            do {
              path = validInputExistPath("\nВведите путь к папке\\файлу:");
            } while (!showSize(path));
            showFileSizeActionMenu(sizeCounter);
            startMainMenu();
            break;
          }
        case COPY_PATH:
          {
            Path path;
            Path copyPath;

            do {
              path = validInputExistPath("\nВведите путь к копируемой папке\\файлу:");
              copyPath = validInputNoExistPath("\nВведите путь для копирования:");
            } while (!isValidCopyPath(path.toFile(), copyPath.toFile()));

            if (copyFolder(path.toFile(), copyPath.toFile())) {
              System.out.println("\nКопирование выполнено!");
            } else {
              System.err.println("\nОШИБКА: -копирование прервано!");
            }

            startMainMenu();
            break;
          }
        default:
          {
            stopProgram();
            break;
          }
      }
    }
  }

  private static void showFileSizeActionMenu(FileSizeCounter sizeCounter) throws IOException, InterruptedException {
    command = ZERO_STATE;
    int[] availableCommands = new int[] {EXIT, GET_PATH_LIST, GET_FAILED_PATH_LIST, GET_ALL_INFO, MAIN_MENU};
    String input;

    do {
      System.out.println(
          "------------------------------------------------------------------------------");
      System.out.println(
              "Доступные команды:   "
                      + GET_PATH_LIST
                      + " - показать файлы каталога;\n"
                      + "                     "
                      + GET_FAILED_PATH_LIST
                      + " - показать каталоги без доступа;\n"
                      + "                     "
                      + GET_ALL_INFO
                      + " - показать всю информацию о каталоге;\n\n"
                      + "                     "
                      + MAIN_MENU
                      + " - выход в главное меню.");
      System.out.println(
          "------------------------------------------------------------------------------");

      System.out.println("Введите команду: ");
      input = new Scanner(System.in).nextLine();
    } while (isInvalidInputCommand(availableCommands, input));

    switch (command) {
      case GET_PATH_LIST: {
        System.out.println(printList(sizeCounter.getFiles()));
        showFileSizeActionMenu(sizeCounter);
        break;
      }
      case GET_FAILED_PATH_LIST: {
        System.out.println(printList(sizeCounter.getAccessDeniedFiles()));
        showFileSizeActionMenu(sizeCounter);
        break;
      }
      case GET_ALL_INFO: {
        System.err.println("В РАЗРАБОТКЕ");
        showFileSizeActionMenu(sizeCounter);
        break;
      }
      case MAIN_MENU: {
        startMainMenu();
        break;
      }
      case EXIT: {
        isRunning = false;
        startMainMenu();
        break;
      }
      default: {
        break;
      }
    }
  }

  /** Создание нового каталога и получение валидного пути к нему */
  private static Path validInputNoExistPath(String message)
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
    return Paths.get(path);
  }

  /** Подсчитывает и выводит размер в читабельном виде */
  private static boolean showSize(Path path) {
    File file = path.toFile();
    if (file.isFile()) {
      System.out.println(file.getAbsolutePath() + "\nРазмер: " + getReadableSize(file.length()));
    } else if (file.isDirectory()) {
      sizeCounter = new FileSizeCounter(path);
      try {
        Files.walkFileTree(path, sizeCounter);
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }

      System.out.println(
          file.getAbsolutePath()
              + "\nФайлов: "
              + sizeCounter.getFiles().size()
              + "\nРазмер: "
              + getReadableSize(sizeCounter.getTotalSize())
              + "\n\nНе удалось прочесть ["
              + sizeCounter.getAccessDeniedFiles().size()
              + "]: "
              + printList(sizeCounter.getAccessDeniedFiles())
              + "\nВсего файлов: "
              + (sizeCounter.getAccessDeniedFiles().size() + sizeCounter.getFiles().size()));
    }

    return true;
  }

  private static String printList(List<Path> list) {
    StringBuilder builder = new StringBuilder().append("\n");
    list.forEach(p -> builder
            .append(p.toString())
            .append("\tразмер: ")
            .append(getReadableSize(p.toFile().length()))
            .append("\n")
    );
    return builder.toString();
  }

  /** Получение пути к существующему файлу/каталогу */
  private static Path validInputExistPath(String message) {
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
    return Paths.get(path);
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

  /** Копирует директорию со всеми вложенными папками и файлами */
  private static boolean copyFolder(File target, File copy) throws InterruptedException {
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
          copy = new File(copy, file);

          TimeUnit.MILLISECONDS.sleep(40);
          copyFolder(targetFile, copy);
        }
      }

    } else if (target.isFile()) {
      // Если копируемый файл является файлом
      // производим его запись
      copyFile(target, copy);
    } else {
      return false;
    }

    return true;
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

  /** Завершение программы */
  private static void stopProgram() {
    isRunning = false;
  }

  /** Класс для обхода директории и сбора данных о файлах в ней */
  static class FileSizeCounter implements FileVisitor<Path> {
    private Path dir;
    private long totalSize;
    private List<Path> files;
    private List<Path> accessDeniedFiles;

    public FileSizeCounter(Path path) {
      dir = path;
      files = new ArrayList<>();
      accessDeniedFiles = new ArrayList<>();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) {
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
      incrementSize(path.toFile().length());
      files.add(path);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e) {
      accessDeniedFiles.add(path);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException e) {
      return FileVisitResult.CONTINUE;
    }

    public List<Path> getFiles() {
      return files;
    }

    public List<Path> getAccessDeniedFiles() {
      return accessDeniedFiles;
    }

    public void incrementSize(long size) {
      this.totalSize += size;
    }

    public long getTotalSize() {
      return this.totalSize;
    }
  }
}
