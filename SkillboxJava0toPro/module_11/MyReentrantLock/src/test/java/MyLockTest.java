import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MyLockTest {

  static final int ACTIONS = 10_000_000;
  static int c = 0;
  static MyLock lock = new MyLock();

  @Test
  public void testLock() throws InterruptedException {
    int expected = c;

    Thread t1 = new Thread(() -> {
      for (int i = 0; i < ACTIONS; i++) {
        lock.lock();
        c++;
        lock.unlock();
      }
    });

    Thread t2 = new Thread(() -> {
      for (int i = 0; i < ACTIONS; i++) {
        lock.lock();
        c--;
        lock.unlock();
      }
    });

    Thread t3 = new Thread(() -> {
      while (MyLock.counter.get() < (ACTIONS * 2)) {
        try {
          sleep(500);
          System.out.printf("%s\n", MyLock.counter.get());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    t3.start();

    t1.start();
    t2.start();
    t1.join();
    t2.join();
    t3.join();

    int actual = c;
    System.out.printf("\nExpected: %s\tActual: %s", expected, actual);
    assertEquals(expected, actual);
  }
}
