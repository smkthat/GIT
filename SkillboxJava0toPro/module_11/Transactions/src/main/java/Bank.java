package main.java;

import java.util.HashMap;
import java.util.Random;

public class Bank {
  private HashMap<String, Account> accounts;
  private final Random random = new Random();

  public Bank() {
    accounts = new HashMap<>();
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

    synchronized (from) {
      synchronized (to) {
        if (from.decrease(amount)) {
          to.increase(amount);
        }

        if (amount > 50000) {
          if (isFraud(fromAccountNum, toAccountNum, amount)) {
            from.blockAccount();
            to.blockAccount();
            Thread.currentThread().interrupt();
          }
        }
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
