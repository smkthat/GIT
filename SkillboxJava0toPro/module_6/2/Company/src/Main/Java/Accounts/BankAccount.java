package Main.Java.Accounts;

import java.time.LocalDate;

public class BankAccount {
  double moneyBalance;
  LocalDate createdDate;

  public BankAccount() {
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

  public void withdrawMoney(double moneyAmount) {
    if (this.moneyBalance <= moneyAmount) {
      System.err.println("Not enough money");
    } else {
      this.moneyBalance = this.moneyBalance - moneyAmount;
    }
  }
}
