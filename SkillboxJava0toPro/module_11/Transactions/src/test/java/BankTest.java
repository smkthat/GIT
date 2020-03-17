package test.java;

import main.java.Account;
import main.java.Bank;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BankTest {

  private Bank createBank() {
    Bank bank = new Bank();
    bank.addAccount(new Account("empty", 0));
    bank.addAccount(new Account("30", 30000));
    bank.addAccount(new Account("50", 50000));
    bank.addAccount(new Account("60", 60000));
    bank.addAccount(new Account("350", 350000));
    bank.addAccount(new Account("400", 400000));

    Account lockedAcc = new Account("blocked", 160000);
    lockedAcc.blockAccount();
    bank.addAccount(lockedAcc);

    return bank;
  }

  @Test
  public void oneThreadTransfer() throws InterruptedException {
    Bank bank = createBank();
    long[] expected = {15000, 15000};
    System.out.println("\n\t\toneThreadTransfer()");

    bank.transfer("30", "empty", 15000);

    long[] actual = {bank.getBalance("30"), bank.getBalance("empty")};
    System.out.println(
        "Expected: " + Arrays.toString(expected) + " Actual: " + Arrays.toString(actual));
    assertArrayEquals(expected, actual);
  }

  @Test
  public void transferFromToBlockedAcc() throws InterruptedException {
    System.out.println("\n\t\ttransferFromBlockedAcc()");
    Bank bank = createBank();
    long[] expected = {160000, 30000};
    bank.transfer("blocked", "30", 20000);
    long[] actual = {bank.getBalance("blocked"), bank.getBalance("30")};
    System.out.println(
        "Expected: " + Arrays.toString(expected) + " Actual: " + Arrays.toString(actual));

    assertArrayEquals(expected, actual);

    long[] expected2 = {30000, 160000};
    bank.transfer("30", "blocked", 15000);
    long[] actual2 = {bank.getBalance("30"), bank.getBalance("blocked")};
    System.out.println(
        "Expected: " + Arrays.toString(expected) + " Actual: " + Arrays.toString(actual));

    assertArrayEquals(expected2, actual2);
  }

  @Test
  public void transferFromAccWithoutMoney() throws InterruptedException {
    System.out.println("\n\t\ttransferFromBlockedAcc()");
    Bank bank = createBank();
    long[] expected = {0, 30000};
    bank.transfer("empty", "30", 2000);
    long[] actual = {bank.getBalance("empty"), bank.getBalance("30")};
    System.out.println(
        "Expected: " + Arrays.toString(expected) + " Actual: " + Arrays.toString(actual));

    assertArrayEquals(expected, actual);
  }

  @Test
  public void multiThreadTransferCheck() throws InterruptedException {
    System.out.println("\n\t\tmultiThreadTransferCheck()");
    Bank bank = createBank();
    long[] expected = {30000, 50000};
    for (int i = 0; i < 10; i++) {
      Thread t =
          new Thread(
              () -> {
                try {
                  System.out.println(Thread.currentThread().getName());
                  bank.transfer("30", "50", 25000);
                  bank.transfer("50", "empty", 25000);
                  bank.transfer("empty", "30", 25000);
                  bank.transfer("50", "empty", 25000);
                  bank.transfer("30", "50", 25000);
                  bank.transfer("empty", "30", 25000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              });
      t.start();
      t.join();
    }

    long[] actual = {bank.getBalance("30"), bank.getBalance("50")};
    System.out.println(
        "Expected: " + Arrays.toString(expected) + " Actual: " + Arrays.toString(actual));

    assertArrayEquals(expected, actual);
  }

  @Test
  public void checkBankBalance() {
    System.out.println("\n\t\tcheckBankBalance()");
    Bank bank = createBank();
    long expected = bank.getBankBalance();
    long actual = 1050000;
    System.out.println("Expected: " + bank.getBankBalance() + " Actual: " + actual);
    assertEquals(expected, actual);
  }

  @Test
  public void accountsWillBeBlocked() throws InterruptedException {
    System.out.println("\n\t\taccountsWillBeBlocked()");
    Bank bank = createBank();
    boolean[] expected = {true, true};
    for (int i = 0; i < 10; i++) {
      Thread t =
          new Thread(
              () -> {
                try {
                  System.out.println(Thread.currentThread().getName());
                  bank.transfer("400", "350", 34000);
                  bank.transfer("350", "400", 27000);
                  bank.transfer("400", "350", 51000);
                  bank.transfer("350", "400", 67000);
                  bank.transfer("350", "400", 15000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              });
      t.start();
      t.join();
    }

    boolean[] actual = {bank.getAccount("400").isBlocked(), bank.getAccount("350").isBlocked()};
    System.out.println(
        "Expected: " + Arrays.toString(expected) + " Actual: " + Arrays.toString(actual));
    assertArrayEquals(expected, actual);
  }
}
