package Main.Java.Clients;

import Main.Java.Accounts.DefaultAccount;

public class IndividualClient extends Client {

  @Override
  public void depositMoney(double money) {
    super.account.depositMoney(money);
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
