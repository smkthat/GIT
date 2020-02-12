import java.io.File;

public class Main {
  private static final String SRC_FOLDER = "d:/images";
  private static final String DST_FOLDER = "d:/output";

  public static void main(String[] args) {
    File srcDir = new File(SRC_FOLDER);

    File[] files = srcDir.listFiles();
    if (files == null) {
      throw new RuntimeException("Can't get files. Check dest folder.");
    }

    System.out.println("Resize starting!");
    System.out.println("-".repeat(50));

    ThreadPool threadPool = new ThreadPool();
    threadPool.startResize(files, 300, DST_FOLDER);
  }
}
