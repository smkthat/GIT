package Main.Java.Accounts;


public class DefaultAccount {

  double moneyBalance;

  public DefaultAccount() {
    moneyBalance = 0.00;
  }

  public double getMoneyBalance() {
    return moneyBalance;
  }

  public void depositMoney(double money) {
    System.out.println("+" + money);
    moneyBalance = moneyBalance + money;
  }

  public void withdrawMoney(double money) {
    System.out.println("-" + money);
    if (moneyBalance <= money) {
      System.err.println("Not enough money! Withdrawal aborted!");
    } else {
      moneyBalance = moneyBalance - money;
    }
  }
}
