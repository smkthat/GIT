import java.util.concurrent.atomic.AtomicInteger;

public class MyLock {

  public static final AtomicInteger COUNTER = new AtomicInteger(0);
  private boolean isLock;

  public synchronized void lock() {
    while (isLock) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    isLock = true;
    COUNTER.incrementAndGet();
  }

  public synchronized void unlock() {
    isLock = false;
    notify();
  }
}
