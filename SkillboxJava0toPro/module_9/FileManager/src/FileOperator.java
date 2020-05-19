import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/** Класс для управления операциями с файлами */
class FileOperator {
  private Path root;

  private FileCopier copier;
  private FileSizeCounter sizeCounter;

  /** Выводит на печать все содержимое директории */
  public void showPaths() {
    System.out.println(pathListToString(sizeCounter.getFiles()));
  }

  /** Выводит на печать пути к которым нет доступа в указанной директории */
  public void showFailedPaths() {
    System.out.println(pathListToString(sizeCounter.getAccessDeniedFiles()));
  }

  /** Создает строку с информацией о собранных файлах */
  private String pathListToString(List<Path> list) {
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
                .append(Helper.getDots(p.toString().length(), maxStringLength.get()))
                .append(Helper.getDots(0, 5))
                .append(" ")
                .append(Helper.getReadableSize(p.toFile().length()))
                .append("\n"));
    return builder.toString();
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
    sizeCounter = new FileSizeCounter();
    System.out.println(sizeCounter.gettingInfo(root));
  }

  /** Запуск процесса копирования файлов */
  public void startCopy() {
    boolean isCopyConfirmed;
    Path copyPath;
    do {
      root = validInputExistPath("\nВведите путь к копируемой папке\\файлу:");
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
      copier = new FileCopier();
      copyDone = copier.copyPath(root, copyPath);
    }

    if (copyDone) {
      System.out.println("\nКопирование выполнено!");
    } else {
      System.err.println("\nКопирование прервано!");
    }

    FileManager.startMainMenu();
  }

  /** Получение пути для копирования */
  private Path validInputCopyPath(String message) {
    System.out.println(message);
    return Paths.get(new Scanner(System.in).nextLine());
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

  /** */
  public String getInfo() {
    return sizeCounter.getInfo();
  }
}
