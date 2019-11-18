package Main.Java.Accounts;

import java.time.LocalDate;

public class DepositoryAccount extends DefaultAccount {

  private LocalDate lastDeposit;

  public void depositMoney(double money) {
    System.out.println("+" + money);
    moneyBalance = moneyBalance + money;
    lastDeposit = LocalDate.now();
  }

  private boolean validateWithdrawDate() {
    LocalDate validWithdrawDate = LocalDate.now().minusMonths(1);
    return validWithdrawDate.isAfter(lastDeposit);
  }

  public void withdrawMoney(double money) {
    System.out.println("-" + money);

    if (validateWithdrawDate()) {
      if (moneyBalance <= money) {
        System.err.println("Not enough money. Withdrawal aborted!");
      } else {
        moneyBalance = moneyBalance - money;
      }
    } else {
      System.err.println("Withdrawal is prohibited!");
    }
  }
}
