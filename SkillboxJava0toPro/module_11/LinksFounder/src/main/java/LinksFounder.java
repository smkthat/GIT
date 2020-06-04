import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class LinksFounder implements LinkHandler {
  public static final String STARTING_URL =
            "https://lenta.ru";
//      "https://secure-headland-59304.herokuapp.com";
  //      "https://skillbox.ru";

  private final Collection<String> visitedLinks = Collections.synchronizedSet(new TreeSet<>());
  private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;
  public static final int MAX_VISITED_LINKS_LENGTH = 300;
  private long startTime = System.currentTimeMillis();

  private final String rootUrl;
  private final ForkJoinPool pool;

  public LinksFounder(String rootUrl) {
    this.rootUrl = fixTail(rootUrl);
    pool = new ForkJoinPool(MAX_THREADS);
  }

  private String fixTail(String link) {
    return link.endsWith("/") ? link : link + "/";
  }

  private void startSearching() {
    startTime = System.currentTimeMillis();
    System.out.print("*********************************************************\n");
    System.out.printf(
        "LinksFounder: Starting with max visited links: %s\n", MAX_VISITED_LINKS_LENGTH);
    System.out.printf("LinksFounder: Start Date: %s\n", new Date(startTime));

    LinkFinderAction action = new LinkFinderAction(this.rootUrl, this);
    pool.execute(action);

    do {
      printProgressInfo();
      try {
        TimeUnit.MILLISECONDS.sleep(5_000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } while (!action.isDone());

    printProgressInfo();

    if (action.isCompletedNormally()) {
      System.out.println("All tasks completed normally.");
    } else {
      action.getException().printStackTrace();
    }

    printExecutionTime();

    pool.shutdown();
  }

  private void printExecutionTime() {
    System.out.printf("LinksFounder: Start Time: %s\n", new Date(startTime));
    System.out.printf("LinksFounder: Finish Time: %s\n", new Date(System.currentTimeMillis()));
    System.out.print("*********************************************************\n");
  }

  private void printProgressInfo() {
    System.out.print("*********************************************************\n");
    System.out.printf("LinksFounder: Active Threads Count: %s\n", pool.getActiveThreadCount());
    System.out.printf("LinksFounder: Targeted Parallelism: %s\n", pool.getParallelism());
    System.out.printf("LinksFounder: Visited Links Count: %s\n", size());
    System.out.printf("LinksFounder: Queued Task Count: %s\n", pool.getQueuedTaskCount());
    System.out.printf("LinksFounder: Steal Count: %s\n", pool.getStealCount());
    System.out.printf(
        "LinksFounder: Links founded: %s\n", LinkFinderAction.linksFoundCounter.get());
    System.out.print("*********************************************************\n");
  }

  @Override
  public int size() {
    return visitedLinks.size();
  }

  @Override
  public boolean isVisited(String link) {
    return visitedLinks.contains(link);
  }

  @Override
  public void addVisited(String link) {
    visitedLinks.add(link);
  }

  public static void main(String[] args) {
    LinksFounder founder = new LinksFounder(STARTING_URL);
    founder.startSearching();

    prepareAndPrintResult(founder);
  }

  public static void prepareAndPrintResult(LinksFounder founder) {
    if (founder.visitedLinks.stream().findFirst().isPresent()) {
      founder.visitedLinks.remove(founder.visitedLinks.stream().findFirst().get());
    }
    System.out.printf("\nResult:\n%s\n", STARTING_URL);
    new ArrayList<>(founder.visitedLinks)
        .subList(0, MAX_VISITED_LINKS_LENGTH - 1)
        .forEach(link -> System.out.printf("%s%s\n", getTabs(link), link));
  }

  private static String getTabs(String link) {
    int count = link.replaceAll("[^/]*", "").length() - 2;
    return "\t".repeat(count);
  }
}
