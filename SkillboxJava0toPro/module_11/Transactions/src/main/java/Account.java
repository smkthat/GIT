package main.java;

import java.util.Objects;

public class Account {
  private long money;
  private String accNumber;
  private volatile boolean blocked;

  public Account(String accNumber, long money) {
    this.accNumber = accNumber;
    this.money = money;
    blocked = false;
  }

  public boolean decrease(long amount) {
    boolean isDecreased = getBalance() >= amount;
    if (isDecreased) {
      money -= amount;
    }
    return isDecreased;
  }

  public void increase(long amount) {
    money += amount;
  }

  public long getBalance() {
    return money;
  }

  public String getAccNumber() {
    return accNumber;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public void unblockAccount() {
    blocked = false;
  }

  public void blockAccount() {
    blocked = true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Account)) return false;
    Account account = (Account) o;
    return accNumber.equals(account.accNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accNumber, money, blocked);
  }
}
