package Main.Java.Clients;

import Main.Java.Accounts.DefaultAccount;

abstract class Client {
  DefaultAccount account;

  abstract public void depositMoney(double money);
  abstract public void withdrawMoney(double money);
  abstract public void getBalance();
  abstract public void setAccount(DefaultAccount account);
}
