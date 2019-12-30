import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

class FileSizeCounter extends SimpleFileVisitor<Path> {
  private Path root;
  private long totalSize;
  private List<Path> files;
  private List<Path> accessDeniedFiles;
  private String info;

  public FileSizeCounter() {
    files = new ArrayList<>();
    accessDeniedFiles = new ArrayList<>();
  }

  private boolean startWalk() {
    if (Files.exists(root)) {
      try {
        Files.walkFileTree(root, this);
        return true;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return false;
  }

  /** Подсчитывает и выводит шнформацию о файле */
  public String gettingInfo(Path target) {
    root = target;
    boolean walkComplete = startWalk();

    if (walkComplete) {
      StringBuilder builder = new StringBuilder();
      try {
        if (Files.isDirectory(root)) {
          builder
              .append(root)
              .append("\nРазмер: ")
              .append(Helper.getReadableSize(getTotalSize()))
              .append("\nФайлов: ")
              .append(getFiles().size())
              .append("\tиз них не удалось прочесть: ")
              .append(getAccessDeniedFiles().size())
              .append("\nВсего: ")
              .append(getAccessDeniedFiles().size() + getFiles().size());
        } else {
          builder
              .append(root)
              .append("\nРазмер: ")
              .append(Helper.getReadableSize(Files.size(root)));
        }
      } catch (IOException e) {
        System.err.println("ОШИБКА: -не удалось получить размер файла.");
        return null;
      }

      info = builder.toString();
    } else {
      System.err.println("ОШИБКА: -ошибка получения информации о " + root.toAbsolutePath());
    }

    return info;
  }

  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
    totalSize += attr.size();
    files.add(path);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attr) {
    files.add(path);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path path, IOException e) {
    accessDeniedFiles.add(path);
    return FileVisitResult.CONTINUE;
  }

  public List<Path> getFiles() {
    return files;
  }

  public List<Path> getAccessDeniedFiles() {
    return accessDeniedFiles;
  }

  public long getTotalSize() {
    return this.totalSize;
  }

  public String getInfo() {
    return info;
  }
}
