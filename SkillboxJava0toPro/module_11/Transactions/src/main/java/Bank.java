package main.java;

import java.util.HashMap;
import java.util.Random;

public class Bank {
  private HashMap<String, Account> accounts;
  private final Random random = new Random();

  public Bank() {
    accounts = new HashMap<>();
  }

  public void setAccounts(HashMap<String,Account> accounts) {
    this.accounts = accounts;
  }

  private void generateRandomAcc(int accountsCount, long minMoney, long maxMoney) {
    for (int i = 0; i < accountsCount; i++) {
      String accNumber = Integer.toString(i + 1);
      accounts.put(
          accNumber,
          new Account(accNumber, random.nextInt((int) (maxMoney - minMoney) + 1) + minMoney));
    }
  }

  public void addAccount(Account account) {
    accounts.put(account.getAccNumber(), account);
  }

  public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
      throws InterruptedException {
    Thread.sleep(1000);
    return random.nextBoolean();
  }

  /**
   * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
   * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
   * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше усмотрение)
   */
  public void transfer(String fromAccountNum, String toAccountNum, long amount)
      throws InterruptedException {
    Account from = accounts.get(fromAccountNum);
    Account to = accounts.get(toAccountNum);

    if (from.isBlocked() || to.isBlocked()) {
      return;
    }

    System.out.println(
        "Transfer "
            + amount
            + " : "
            + fromAccountNum
            + "["
            + from.getBalance()
            + "]"
            + " -> "
            + toAccountNum
            + "["
            + to.getBalance()
            + "]");

    if (from.hashCode() < to.hashCode()) {
      synchronized (from) {
        synchronized (to) {
          doTransfer(from, to, amount);
        }
      }
    } else {
      synchronized (to) {
        synchronized (from) {
          doTransfer(from, to, amount);
        }
      }
    }
  }

  private void doTransfer(final Account from, final Account to, final long amount)
      throws InterruptedException {
    if (from.decrease(amount)) {
      to.increase(amount);
    }

    if (amount > 50000) {
      if (isFraud(from.getAccNumber(), to.getAccNumber(), amount)) {
        from.blockAccount();
        to.blockAccount();
        Thread.currentThread().interrupt();
      }
    }
  }

  /** TODO: реализовать метод. Возвращает остаток на счёте. */
  public long getBalance(String accountNum) {
    return accounts.get(accountNum).getBalance();
  }

  public long getBankBalance() {
    return accounts.values().stream().map(Account::getBalance).mapToLong(Long::longValue).sum();
  }

  public Account getAccount(String accountNum) {
    return accounts.get(accountNum);
  }
}
