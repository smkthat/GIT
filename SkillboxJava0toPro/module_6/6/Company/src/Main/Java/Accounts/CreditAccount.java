package Main.Java.Accounts;


public class CreditAccount extends DefaultAccount {

  private static final double FEE = 0.01;

  private double calcWithdrawFee(double money) {
    double amountFee = money * FEE;
    System.out.println("Withdraw account amount of fee: " + amountFee);
    return amountFee;
  }

  public void withdrawMoney(double money) {
    double withdrawAmount = money + calcWithdrawFee(money);
    System.out.println("-" + withdrawAmount);
    if (moneyBalance <= withdrawAmount) {
      System.err.println("Not enough money. Withdrawal aborted!");
    } else {
      moneyBalance = moneyBalance - withdrawAmount;
    }
  }
}
