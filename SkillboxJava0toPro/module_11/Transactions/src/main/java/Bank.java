package main.java;

import java.util.HashMap;
import java.util.Random;

public class Bank {
  private HashMap<String, Account> accounts;
  private final Random random = new Random();

  public Bank() {
    accounts = new HashMap<>();
  }

  public void setAccounts(HashMap<String, Account> accounts) {
    this.accounts = accounts;
  }

  public HashMap<String, Account> getAccounts() {
    return accounts;
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

    System.out.printf(
        "\nTransfer %s : %s[%s] -> %s[%s]",
        amount, from.getAccNumber(), from.getBalance(), to.getAccNumber(), to.getBalance());

    if (isNotCorrectTransfer(from, to)) {
      return;
    }

    if (from.getId().equals(to.getId())) {
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

  private boolean isNotCorrectTransfer(Account from, Account to) {
    return from.equals(to) || from.isBlocked() || to.isBlocked();
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
      }
    }
  }

  /** TODO: реализовать метод. Возвращает остаток на счёте. */
  public long getBalance(String accountNum) {
    Account account = accounts.get(accountNum);
    synchronized (account) {
      return account.getBalance();
    }
  }

  public long getBankBalance() {
    return accounts.values().stream()
        .map(
            account -> {
              synchronized (account) {
                return account.getBalance();
              }
            })
        .mapToLong(Long::longValue)
        .sum();
  }

  public Account getAccount(String accountNum) {
    return accounts.get(accountNum);
  }
}
