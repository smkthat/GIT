package Main.Java.Accounts;

import java.time.LocalDate;

public class CreditAccount extends DefaultAccount {
  private static final double FEE = 0.01;

  public CreditAccount() {
    this.moneyBalance = 0.00;
    this.createdDate = LocalDate.now();
  }

  private double calcWithdrawFee(double money) {
    double amountFee = money * FEE;
    System.out.println("Withdraw account amount of fee: " + amountFee);
    return amountFee;
  }

  public void withdrawMoney(double money) {
    double withdrawAmount = money + calcWithdrawFee(money);
    System.out.println("-" + withdrawAmount);
    if (this.moneyBalance <= withdrawAmount) {
      System.err.println("Not enough money. Withdrawal aborted!");
    } else {
      this.moneyBalance = this.moneyBalance - withdrawAmount;
    }
  }
}
