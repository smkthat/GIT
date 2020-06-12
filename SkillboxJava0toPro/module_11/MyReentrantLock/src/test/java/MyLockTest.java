import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MyLockTest {

  static int c = 0;
  static MyLock lock = new MyLock();

  @Test
  public void testLock() throws InterruptedException {
    int expected = c;

    MyLock lock = new MyLock();
    Thread t1 = new Thread(() -> {
      for (int i = 0; i < 10_000; i++) {
        lock.lock();
        c++;
        lock.unlock();
      }
    });

    Thread t2 = new Thread(() -> {
      for (int i = 0; i < 10_000; i++) {
        lock.lock();
        c--;
        lock.unlock();
      }
    });

    t1.start();
    t2.start();
    t1.join();
    t2.join();

    int actual = c;
    assertEquals(expected, actual);
  }
}
