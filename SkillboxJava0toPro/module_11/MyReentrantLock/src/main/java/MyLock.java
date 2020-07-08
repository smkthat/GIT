import java.util.concurrent.atomic.AtomicInteger;

public class MyLock {

  public static AtomicInteger counter = new AtomicInteger(0);
  private boolean lock;

  public synchronized void lock() {
    if (this.lock) {
      while (lock) {
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      lock();
    } else {
      lock = true;
      counter.getAndIncrement();
      notify();
    }
  }

  public synchronized void unlock() {
    lock = false;
  }
}
