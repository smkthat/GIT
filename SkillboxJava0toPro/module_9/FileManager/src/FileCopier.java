import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class FileCopier extends SimpleFileVisitor<Path> {
  private Path target;
  private Path copyTo;

  private static final String SEPARATOR_PATH = "/";

  public boolean copyPath(Path target, Path copyTo) {
    this.target = target;
    this.copyTo = generateCopyPath(target, copyTo);

    if (!createCopyDirectories(this.target, this.copyTo)) {
      return false;
    }

    return startCopy();
  }

  private boolean startCopy() {
    try {
      Files.walkFileTree(target, this);
    } catch (IOException e) {
      System.err.println("ОШИБКА: -невозможно прочитать путь.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /** Проверка на существование каталога и его подкаталогов */
  private boolean createCopyDirectories(Path target, Path copy) {
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

  /** Создает новый путь для копии */
  private Path changePaths(Path path, Path target, Path replacement) {
    String tar = target.toString();
    String rep = replacement.toString();
    return Paths.get(path.toString().replace(tar, rep));
  }

  /** Генерируем путь к копии */
  private Path generateCopyPath(Path target, Path copy) {
    return Paths.get(copy.toString() + SEPARATOR_PATH + target.getFileName());
  }

  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
    Path copy = changePaths(path, target, copyTo);

    if (!Files.exists(copy)) {
      try {
        Files.copy(path, copy);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attr) {
    Path copy = changePaths(path, target, copyTo);

    if (!Files.exists(copy)) {
      try {
        Files.createDirectory(copy);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return FileVisitResult.CONTINUE;
  }
}
