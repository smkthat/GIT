package Main.Java.Clients;

import Main.Java.Accounts.DefaultAccount;

public class EntityClient extends Client {

  private static final double WITHDRAW_FEE = 0.01;

  @Override
  public void depositMoney(double money) {
    account.depositMoney(money);
  }

  private double calcWithdrawFee(double money) {
    double amountFee = money * WITHDRAW_FEE;
    System.out.println("Withdraw client amount of fee: " + amountFee);
    return amountFee;
  }

  @Override
  public void withdrawMoney(double money) {
    double withdrawAmount = money + calcWithdrawFee(money);
    account.withdrawMoney(withdrawAmount);
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
