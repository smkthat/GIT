import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
  private ExecutorService threadPool;
  private List<Callable<Integer>> taskList;

  private final int SYS_LOGIC_PROCESSORS_COUNT = Runtime.getRuntime().availableProcessors();

  public ThreadPool() {
    this.threadPool = Executors.newFixedThreadPool(SYS_LOGIC_PROCESSORS_COUNT);
    this.taskList = new ArrayList<>();
  }

  public void startResize(File[] files, int newWidth, String dstFolder) {
    System.out.println("\t* creating tasks:");
    int partLength = files.length / SYS_LOGIC_PROCESSORS_COUNT;
    int lastPartLength = files.length - (partLength * SYS_LOGIC_PROCESSORS_COUNT) + partLength;

    System.out.println("\t\timages founded: " + files.length);
    System.out.println("\t\tlogic processors available: " + SYS_LOGIC_PROCESSORS_COUNT);
    System.out.println("\t\tparts of files calculated: " + SYS_LOGIC_PROCESSORS_COUNT);
    System.out.println("\t\taverage number per part: " + partLength);

    for (int i = 0; i < SYS_LOGIC_PROCESSORS_COUNT; i++) {
      File[] filesPart;
      if (i + 1 == SYS_LOGIC_PROCESSORS_COUNT) {
        filesPart = new File[lastPartLength];
        System.arraycopy(
            files, (SYS_LOGIC_PROCESSORS_COUNT - 1) * partLength, filesPart, 0, filesPart.length);
      } else {
        filesPart = new File[partLength];
        System.arraycopy(files, i * partLength, filesPart, 0, filesPart.length);
      }

      taskList.add(new ImageSizeChanger(dstFolder, filesPart, newWidth));
    }

    if (!startExecutingTasks()) {
      System.err.println("Troubles founded!");
    }
  }

  private boolean startExecutingTasks() {
    System.out.println("\t* execute tasks:");
    int sum = 0;
    try {
      sum =
          threadPool.invokeAll(taskList).stream()
              .map(
                  f -> {
                    try {
                      return f.get();
                    } catch (Exception e) {
                      throw new IllegalStateException(e);
                    }
                  })
              .mapToInt(Integer::intValue)
              .sum();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      threadPool.shutdown();
      System.out.println("-".repeat(50));
      System.out.println("Complete!");
    }

    return SYS_LOGIC_PROCESSORS_COUNT == sum;
  }
}
