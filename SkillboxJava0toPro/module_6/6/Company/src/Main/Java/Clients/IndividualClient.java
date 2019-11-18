package Main.Java.Clients;

import Main.Java.Accounts.DefaultAccount;

public class IndividualClient extends Client {

  @Override
  public void depositMoney(double money) {
    account.depositMoney(money);
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
