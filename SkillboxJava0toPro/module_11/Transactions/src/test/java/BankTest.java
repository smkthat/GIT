package test.java;

import main.java.Account;
import main.java.Bank;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class BankTest {

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
  public void multiThreadTransferCheck() {
    Bank bank = createBankWithTenAccounts();
    Object[] expected = Stream.generate(() -> 500_000L).limit(10).toArray();

    ExecutorService executor = Executors.newFixedThreadPool(100);
    List<Callable<Integer>> transfersTasks = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      for (int j = 1; j <= 10; j++) {
        final int f = j;
        transfersTasks.add(() -> {
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
      notExecutedTasks = executor.invokeAll(transfersTasks).stream()
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
    // создаем банк с 10 аккаунтами
    Bank bank = createBankWithTenAccounts();
    Object[] expected = Stream.generate(() -> 500_000L).limit(10).toArray();

    // готовим 11 потоков
    ExecutorService executor = Executors.newFixedThreadPool(11);
    ReadWriteLock rw = new ReentrantReadWriteLock();
    // блокируем writeLock, чтобы 10 потоков ждали первого.
    Lock lock = rw.writeLock();
    // создаем первый поток
    executor.submit(() -> {
      // он должен заблочить все акакаунты
      bank.getAccounts().values().forEach(Account::blockAccount);
      // далее он открывает лок и говорит 10 потокам - фас
      lock.unlock();
    });

    // создаем 10 потоков
    for (int threads = 1; threads <= 10; threads++) {
      executor.submit(() -> {
        // в этой точке 10 потоков замируют в ожидании lock.unlock();
        rw.readLock().lock();
        // и пробуют совершить трансфер с любого на любой аккаунт с 1 рублем
        try {
          bank.transfer("1","10",1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    }

    // ждем пока всё уляжется
    try {
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException ex) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    Object[] actual = bank.getAccounts().values().stream().map(Account::getBalance).toArray();

    // проверяем что балансы аккаунтов не поменялись.
    assertArrayEquals(expected,actual);
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
