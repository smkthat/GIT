package test.java;

import main.java.Account;
import main.java.Bank;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TransfersTest {

  private Bank createBank() {
    Bank bank = new Bank();
    bank.addAccount(new Account("empty", 0));
    bank.addAccount(new Account("30", 30_000L));
    bank.addAccount(new Account("50", 50_000L));
    bank.addAccount(new Account("60", 60_000L));
    bank.addAccount(new Account("350", 350_000L));
    bank.addAccount(new Account("400", 400_000L));

    Account lockedAcc = new Account("blocked", 160_000L);
    lockedAcc.blockAccount();
    bank.addAccount(lockedAcc);

    return bank;
  }

  private Bank createBankWithTenAccounts() {
    Bank bank = new Bank();
    HashMap<String, Account> accounts =
        IntStream.rangeClosed(1, 10)
            .boxed()
            .map(String::valueOf)
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    s -> new Account(s, 500_000L),
                    (e1, e2) -> e1,
                    HashMap::new));
    bank.setAccounts(accounts);
    return bank;
  }

  @Test
  public void oneThreadTransfer() throws InterruptedException {
    Bank bank = createBank();
    long[] expected = {15_000L, 15_000L};
    bank.transfer("30", "empty", 15_000L);
    long[] actual = {bank.getBalance("30"), bank.getBalance("empty")};

    assertArrayEquals(expected, actual);
  }

  @Test
  public void transferFromToBlockedAcc() throws InterruptedException {
    Bank bank = createBank();
    long[] expected = {160_000L, 30_000L};
    bank.transfer("blocked", "30", 20_000L);
    long[] actual = {bank.getBalance("blocked"), bank.getBalance("30")};

    assertArrayEquals(expected, actual);

    long[] expected2 = {30_000L, 160_000L};
    bank.transfer("30", "blocked", 15_000L);
    long[] actual2 = {bank.getBalance("30"), bank.getBalance("blocked")};

    assertArrayEquals(expected2, actual2);
  }

  @Test
  public void transferFromAccWithoutMoney() throws InterruptedException {
    Bank bank = createBank();
    long[] expected = {0, 30_000L};
    bank.transfer("empty", "30", 2_000L);
    long[] actual = {bank.getBalance("empty"), bank.getBalance("30")};

    assertArrayEquals(expected, actual);
  }

  @Test(timeout = 20_000) // ms
  public void multiThreadTransferCheck() {
    Bank bank = createBankWithTenAccounts();
    Object[] expected = Stream.generate(() -> 500_000L).limit(10).toArray();

    ExecutorService executor = Executors.newFixedThreadPool(100);
    List<Callable<Integer>> transfersTasks = new ArrayList<>();

    for (int i = 0; i < 10000; i++) {
      for (int j = 1; j <= 10; j++) {
        final int f = j;
        transfersTasks.add(
            () -> {
              try {
                bank.transfer(Integer.toString(f), Integer.toString(11 - f), 1_000L);
              } catch (InterruptedException e) {
                e.printStackTrace();
                return 1;
              }

              return 0;
            });
      }
    }

    int notExecutedTasks = 0;
    try {
      notExecutedTasks =
          executor.invokeAll(transfersTasks).stream()
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
      executor.shutdown();
    }

    try {
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException ex) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    Object[] actual =
        IntStream.rangeClosed(1, 10).boxed().map(String::valueOf).map(bank::getBalance).toArray();

    assertArrayEquals(actual, expected);
  }

  @Test
  public void happensBeforeBlock() {
    Bank bank = createBankWithTenAccounts();
    Object[] expected = Stream.generate(() -> 500_000L).limit(10).toArray();

    ExecutorService executor = Executors.newFixedThreadPool(11);
    ReadWriteLock lock = new ReentrantReadWriteLock();

    System.out.println("write lock");
    executor.submit(
        () -> {
          lock.writeLock().lock();
          try {
            System.out.println("blocking accounts");
            Thread.sleep(3000);
            bank.getAccounts().values().forEach(Account::blockAccount);
            System.out.println("all accounts blocked");
          } catch (InterruptedException e) {
            e.printStackTrace();
          } finally {
            System.out.println("write unlock");
            lock.writeLock().unlock();
          }
        });

    for (int threads = 1; threads <= 10; threads++) {
      //System.out.println("create transfers threads");
      executor.submit(
          () -> {
             lock.readLock().lock();
             //System.out.println("write lock");
            try {
              bank.transfer("1", "10", 1);
            } catch (InterruptedException e) {
              e.printStackTrace();
            } finally {
               //System.out.println("read unlock");
               lock.readLock().unlock();
            }
          });
    }

    try {
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException ex) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    Object[] actual = bank.getAccounts().values().stream().map(Account::getBalance).toArray();

    assertArrayEquals(expected, actual);
  }

  @Test
  public void checkBankBalance() {
    Bank bank = createBank();
    long expected = bank.getBankBalance();
    long actual = 1_050_000L;
    assertEquals(expected, actual);
  }

  @Test
  public void accountsWillBeBlocked() throws InterruptedException {
    Bank bank = createBank();
    boolean[] expected = {true, true};
    for (int i = 0; i < 10; i++) {
      Thread t =
          new Thread(
              () -> {
                try {
                  bank.transfer("400", "350", 34_000L);
                  bank.transfer("350", "400", 27_000L);
                  bank.transfer("400", "350", 51_000L);
                  bank.transfer("350", "400", 67_000L);
                  bank.transfer("350", "400", 15_000L);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              });
      t.start();
      t.join();
    }

    boolean[] actual = {bank.getAccount("400").isBlocked(), bank.getAccount("350").isBlocked()};

    assertArrayEquals(expected, actual);
  }
}
