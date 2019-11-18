package Main.Java.Clients;

import Main.Java.Accounts.DefaultAccount;

public class Businessman extends Client {

  private static final double DEPOSIT_FEE1 = 0.01;
  private static final double DEPOSIT_FEE2 = 0.005;

  @Override
  public void depositMoney(double money) {
    double depositAmount;
    if (money < 1000) {
      depositAmount = money - calcDepositFee(money, DEPOSIT_FEE1);
    } else {
      depositAmount = money - calcDepositFee(money, DEPOSIT_FEE2);
    }
    account.depositMoney(depositAmount);
  }

  private double calcDepositFee(double money, double fee) {
    double amountFee = money * fee;
    System.out.println("Deposit client amount of fee: " + amountFee);
    return amountFee;
  }

  @Override
  public void withdrawMoney(double money) {
    account.withdrawMoney(money);
  }

  @Override
  public void getBalance() {
    System.out.println("Balance: " + account.getMoneyBalance());
  }

  @Override
  public void setAccount(DefaultAccount account) {
    this.account = account;
  }
}
