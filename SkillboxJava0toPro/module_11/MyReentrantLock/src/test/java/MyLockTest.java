import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MyLockTest {

  static final int ACTIONS = 10_000;
  static int c = 0;
  static MyLock lock = new MyLock();

  @Test
  public void testLock() throws InterruptedException {
    int expected = c;

    Thread t1 =
        new Thread(
            () -> {
              for (int i = 0; i < ACTIONS; i++) {
                lock.lock();
                c++;
                try {
                  sleep(0);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                System.out.printf("t1:\t%s\n", c);
                lock.unlock();
              }
            });

    Thread t2 =
        new Thread(
            () -> {
              for (int i = 0; i < ACTIONS; i++) {
                lock.lock();
                c--;
                try {
                  sleep(0);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                System.out.printf("t2:\t%s\n", c);
                lock.unlock();
              }
            });

    t1.start();
    t2.start();
    t1.join();
    t2.join();

    int actual = c;
    int expectedCounter = ACTIONS * 2;
    int actualCounter = MyLock.COUNTER.intValue();

    System.out.printf("\n* Result *\nExpected: %s\tActual: %s", expected, actual);
    System.out.printf("\n\n* Actions* \nExpected: %s\tActual: %s", expectedCounter, actualCounter);

    assertEquals(expected, actual);
    assertEquals(expectedCounter, actualCounter);
  }
}
