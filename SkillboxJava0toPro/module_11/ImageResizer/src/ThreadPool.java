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
    int taskFileLength = files.length / SYS_LOGIC_PROCESSORS_COUNT;
    int different = files.length - taskFileLength * SYS_LOGIC_PROCESSORS_COUNT;

    System.out.println("\t\timages founded: " + files.length);
    System.out.println("\t\tlogic processors available: " + SYS_LOGIC_PROCESSORS_COUNT);
    System.out.println("\t\ttasks of files calculated: " + SYS_LOGIC_PROCESSORS_COUNT);
    System.out.println("\t\taverage number per task: " + taskFileLength + "\n");

    int filesPerTaskCounted = 0;
    for (int i = 0; i < SYS_LOGIC_PROCESSORS_COUNT; i++) {
      File[] filesTask;
      if (different != 0 && i < different) {
        filesTask = new File[taskFileLength + 1];
      } else {
        filesTask = new File[taskFileLength];
      }

      System.arraycopy(files, filesPerTaskCounted, filesTask, 0, filesTask.length);
      filesPerTaskCounted += filesTask.length;
      System.out.println("\t\t\t\t\t\t task " + (i + 1) + ": " + filesTask.length);
      taskList.add(new ImageSizeChanger(dstFolder, filesTask, newWidth));
    }

    System.out.println("\t\t\t\t\t\t ----------");
    System.out.println("\t\t\t\t\t\t  total: " + filesPerTaskCounted);

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
