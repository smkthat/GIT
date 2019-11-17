package Main.Java.Accounts;

import java.time.LocalDate;

public class DepositoryAccount extends BankAccount {
  private LocalDate lastIncreased;

  public DepositoryAccount() {
    this.moneyBalance = 0.00;
    this.createdDate = LocalDate.now();
  }

  public void depositMoney(double money) {
    System.out.println("+" + money);
    this.moneyBalance = money;
    this.lastIncreased = LocalDate.now();
  }

  public void withdrawMoney(double money) {
    if (LocalDate.now().minusMonths(1).isAfter(lastIncreased)) {
      if (this.moneyBalance <= money) {
        System.err.println("Not enough money");
      } else {
        System.out.println("-" + money);
        this.moneyBalance = this.moneyBalance - money;
      }
    } else {
      System.err.println("Withdrawal is prohibited!");
    }
  }
}
