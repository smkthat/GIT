public class MyLock {

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
      notify();
    }
  }

  public synchronized void unlock() {
    lock = false;
    notify();
  }
}
