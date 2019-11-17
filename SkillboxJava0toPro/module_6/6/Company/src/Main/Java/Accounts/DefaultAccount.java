package Main.Java.Accounts;

import java.time.LocalDate;

public class DefaultAccount {
  double moneyBalance;
  LocalDate createdDate;

  public DefaultAccount() {
    this.moneyBalance = 0.00;
    this.createdDate = LocalDate.now();
  }

  public double getMoneyBalance() {
    return moneyBalance;
  }

  public void depositMoney(double money) {
    System.out.println("+" + money);
    this.moneyBalance += money;
  }

  public void withdrawMoney(double money) {
    System.out.println("-" + money);
    if (this.moneyBalance <= money) {
      System.err.println("Not enough money! Withdrawal aborted!");
    } else {
      this.moneyBalance = this.moneyBalance - money;
    }
  }
}
