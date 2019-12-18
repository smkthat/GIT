import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/** Класс для управления операциями с файлами */
class FileOperator extends SimpleFileVisitor<Path> {
  private static final String SEPARATOR_PATH = "\\";

  private Path root;
  private Path copyPath;
  private long totalSize;
  private List<Path> content;
  private List<Path> accessDeniedFiles;
  private String info;

  public FileOperator() {
    content = new ArrayList<>();
    accessDeniedFiles = new ArrayList<>();
  }

  private void walkingLikeImTalking() {
    try {
      Files.walkFileTree(root, this);
    } catch (IOException e) {
      System.err.println("ОШИБКА: -невозможно прочитать путь.");
      System.exit(-1);
    }
  }

  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
    totalSize += attr.size();
    content.add(path);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attr) {
    content.add(path);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path path, IOException e) {
    accessDeniedFiles.add(path);
    return FileVisitResult.CONTINUE;
  }

  /** Создает строку с информацией о собранных файлах */
  public String pathListToString(List<Path> list) {
    if (list.isEmpty()) {
      System.err.println("Нечего печатать!");
      return "";
    }

    var maxStringLength = new AtomicInteger();
    StringBuilder builder = new StringBuilder().append("\n");
    list.forEach(
        p -> {
          int pathLength = p.toString().length();
          if (pathLength > maxStringLength.get()) {
            maxStringLength.set(pathLength);
          }
        });

    list.forEach(
        p ->
            builder
                .append(p.toString())
                .append(getSpaces(p.toString().length(), maxStringLength.get()))
                .append(getSpaces(0, 10))
                .append(" ")
                .append(getReadableSize(p.toFile().length()))
                .append("\n"));
    return builder.toString();
  }

  /** Создаем отступы в зависимости от уровня папки в которой находится File */
  public static String getTab(int level) {
    return "\t".repeat(Math.max(0, level));
  }

  /** Получаем пробелы */
  private String getSpaces(int start, int max) {
    int count = max - start;
    return " ".repeat(count);
  }

  /** Подсчитывает и выводит шнформацию о файле */
  public String getInfoToString() {
    StringBuilder builder = new StringBuilder();
    try {
      if (Files.isDirectory(root)) {
        builder
            .append(root)
            .append("\nРазмер: ")
            .append(getReadableSize(getTotalSize()))
            .append("\nФайлов: ")
            .append(getFiles().size())
            .append("\tиз них не удалось прочесть: ")
            .append(getAccessDeniedFiles().size())
            .append("\nВсего: ")
            .append(getAccessDeniedFiles().size() + getFiles().size());
      } else {
        builder.append(root).append("\nРазмер: ").append(getReadableSize(Files.size(root)));
      }
    } catch (IOException e) {
      System.err.println("ОШИБКА: -не удалось получить размер файла.");
      return null;
    }

    info = builder.toString();
    return info;
  }

  /** Переводит размер файлов к читабельному виду */
  public static String getReadableSize(long size) {
    String[] units = new String[] {"B", "KB", "MB", "GB", "TB"};
    if (size == 0) {
      return "0 " + units[0];
    }

    int unitIndex = (int) (Math.log10(size) / 3);
    double unitValue = 1 << (unitIndex * 10);

    return new DecimalFormat("#,##0.##").format(size / unitValue) + " " + units[unitIndex];
  }

  /** Получение пути к существующему файлу/каталогу */
  private Path validInputExistPath(String message) {
    Path validPath;
    boolean isValidPath;
    do {
      System.out.println(message);
      validPath = Paths.get(new Scanner(System.in).nextLine());
      isValidPath = Files.exists(validPath);
      if (!isValidPath) {
        System.err.println("ОШИБКА: -некорректный путь.");
      }
    } while (!isValidPath);
    return validPath;
  }

  /** Получение информации о размере */
  public void sizeInfo() {
    do {
      root = validInputExistPath("\nВведите путь к папке\\файлу:");
    } while (root == null);
    setRootAndWalk(root);
    System.out.println(getInfoToString());
  }

  /** Запуск процесса копирования файлов */
  public void startCopy() {
    boolean isCopyConfirmed;
    do {
      root = validInputExistPath("\nВведите путь к копируемой папке\\файлу:");
      setRootAndWalk(root);
      copyPath = validInputCopyPath("\nВведите путь для копирования:");

      if (!Files.exists(copyPath)) {
        System.out.println(
            "Создать каталоги и подкаталоги по указанному пути?\n" + copyPath.toAbsolutePath());
        isCopyConfirmed = FileManager.showConfirmCopyActionMenu();
      } else {
        isCopyConfirmed = true;
      }
    } while (!isValidCopyPath(root, copyPath));

    boolean copyDone = false;
    if (isCopyConfirmed) {
      if (Files.isDirectory(root)) {
        copyDone = copyFolder(root, copyPath);
      } else {
        copyDone = copyFile(root, copyPath);
      }
    }

    if (copyDone) {
      System.out.println("\nКопирование выполнено!");
    } else {
      System.err.println("\nКопирование прервано!");
    }

    FileManager.startMainMenu();
  }

  private boolean copyFile(Path target, Path copy) {
    if (!Files.exists(copy)) {
      try {
        Files.createDirectories(copy);
      } catch (IOException e) {
        System.err.println("Ошибка: -невозможно создать директории для.\n" + copy.toAbsolutePath());
        return false;
      }
    }

    if (Files.exists(copy)) {
      Path copyFilePath = generateCopyFilePath(target, copy);
      if (!Files.exists(copyFilePath)) {
        try {
          Files.copy(target, copyFilePath);
        } catch (IOException e) {
          System.err.println("Ошибка: -копирование файла закончилось неудачей.");
        }
        setRootAndWalk(copyFilePath.toAbsolutePath());
        copyPath = null;
        System.out.println(getPathInfo(copyFilePath));
        return true;
      } else {
        System.err.println("ОШИБКА: -файл " + copyFilePath + " уже существует.");
      }
    }

    return false;
  }

  public String getPathInfo(Path path) {
    StringBuilder builder = new StringBuilder();
    if (!Files.isDirectory(path)) {
      builder
          .append("Имя: ")
          .append(path.getFileName())
          .append("\n")
          .append(path.toAbsolutePath())
          .append("\n")
          .append("Размер: ");
      try {
        builder.append(getReadableSize(Files.size(path)));
      } catch (IOException e) {
        System.err.println("Ошибка: -не удалось получить размер файла.");
      }
    } else {
      setRootAndWalk(path);
      return getInfoToString();
    }
    return builder.toString();
  }

  /** Генерируем путь к копии файла */
  private Path generateCopyFilePath(Path target, Path copy) {
    return Paths.get(copy + SEPARATOR_PATH + target.getFileName());
  }

  /** Получение пути для копирования */
  private Path validInputCopyPath(String message) {
    System.out.println(message);
    return Paths.get(new Scanner(System.in).nextLine());
  }

  /** Создает новый путь для копии */
  private Path changePaths(Path path, Path target, Path replacement) {
    String tar = target.toString();
    String rep = replacement.toString();
    return Paths.get(path.toString().replace(tar, rep));
  }

  /** Проверка на существование каталога и его подкаталогов */
  private boolean isExistsDirectory(Path target, Path copy) {
    if (!Files.exists(copy)) {
      if (Files.isDirectory(target)) {
        try {
          Files.createDirectories(copy);
        } catch (IOException e) {
          System.err.println("Ошибка создания каталога");
          return false;
        }
      }
    }

    return true;
  }

  /** Проверка пути копирования */
  private boolean isValidCopyPath(Path path, Path copyPath) {
    if (path == null || copyPath == null) {
      System.err.println(
          "ОШИБКА: -невозможно скопировать файл или папку."
              + "\nПожалуйста, введите другой путь источника или копирования.");
      return false;
    }

    if (path.compareTo(copyPath) == 0) {
      System.err.println(
          "ОШИБКА: -совпадение источника с копированием, невозможно перезаписать файл."
              + "\nПожалуйста, введите другой путь источника или копирования.");
      return false;
    }

    return true;
  }

  /** Копирует директорию со всеми вложенными папками и файлами */
  public boolean copyFolder(Path target, Path copy) {
    if (!isExistsDirectory(target, copy)) {
      return false;
    }

    copy = Paths.get(copy.toString() + SEPARATOR_PATH + target.getName(0));
    setRootAndWalk(copy);

    List<Path> files = getFiles();
    for (Path file : files) {
      Path newCopyPath = changePaths(file, target, copy);
      try {
        if (!Files.exists(newCopyPath)) {
          Files.copy(file, newCopyPath, StandardCopyOption.COPY_ATTRIBUTES);
        }
      } catch (IOException e) {
        System.err.println("ОШИБКА: -не удалось записать файл:\n" + newCopyPath);
      }
    }

    setRootAndWalk(copy);
    System.out.println(pathListToString(getFiles()));
    return true;
  }

  public List<Path> getFiles() {
    return content;
  }

  public List<Path> getAccessDeniedFiles() {
    return accessDeniedFiles;
  }

  public long getTotalSize() {
    return this.totalSize;
  }

  /** Устанавливает корневую дерикторию и собирает информацию о ней */
  private void setRootAndWalk(Path path) {
    root = path;
    walkingLikeImTalking();
  }

  public String getInfo() {
    return info;
  }
}
