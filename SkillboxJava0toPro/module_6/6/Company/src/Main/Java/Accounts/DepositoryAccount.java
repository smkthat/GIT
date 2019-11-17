package Main.Java.Accounts;

import java.time.LocalDate;

public class DepositoryAccount extends DefaultAccount {
  private LocalDate lastDeposit;

  public DepositoryAccount() {
    this.moneyBalance = 0.00;
    this.createdDate = LocalDate.now();
  }

  public void depositMoney(double money) {
    System.out.println("+" + money);
    this.moneyBalance = money;
    this.lastDeposit = LocalDate.now();
  }

  private boolean validateWithdrawDate() {
    LocalDate validWithdrawDate = LocalDate.now().minusMonths(1);
    return validWithdrawDate.isAfter(lastDeposit);
  }

  public void withdrawMoney(double money) {
    System.out.println("-" + money);

    if (validateWithdrawDate()) {
      if (this.moneyBalance <= money) {
        System.err.println("Not enough money. Withdrawal aborted!");
      } else {
        this.moneyBalance = this.moneyBalance - money;
      }
    } else {
      System.err.println("Withdrawal is prohibited!");
    }
  }
}
