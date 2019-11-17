package Main.Java.Clients;

import Main.Java.Accounts.DefaultAccount;

public class Businessman extends Client {
  private static final double DEPOSIT_FEE1 = 0.01;
  private static final double DEPOSIT_FEE2 = 0.005;

  @Override
  public void depositMoney(double money) {
    if (money < 1000) {
      double depositAmount = money - calcDepositFee(money, DEPOSIT_FEE1);
      super.account.depositMoney(depositAmount);
    } else {
      double depositAmount = money - calcDepositFee(money, DEPOSIT_FEE2);
      super.account.depositMoney(depositAmount);
    }
  }

  private double calcDepositFee(double money, double fee) {
    double amountFee = money * fee;
    System.out.println("Deposit client amount of fee: " + amountFee);
    return amountFee;
  }

  @Override
  public void withdrawMoney(double money) {
    super.account.withdrawMoney(money);
  }

  @Override
  public void getBalance() {
    System.out.println("Balance: " + super.account.getMoneyBalance());
  }

  @Override
  public void setAccount(DefaultAccount account) {
    super.account = account;
  }
}
