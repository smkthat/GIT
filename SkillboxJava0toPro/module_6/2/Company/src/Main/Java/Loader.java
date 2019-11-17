package Main.Java;

import Main.Java.Accounts.BankAccount;
import Main.Java.Accounts.CreditAccount;
import Main.Java.Accounts.DepositoryAccount;

public class Loader {

  public static void main(String[] args) {
    BankAccount account = new BankAccount();
    account.depositMoney(1000);
    account.withdrawMoney(2000);
    System.out.println("Default account balance:\t" + account.getMoneyBalance() + "\n");

    BankAccount depositAccount = new DepositoryAccount();
    depositAccount.depositMoney(1000);
    depositAccount.withdrawMoney(500);
    System.out.println("Depository account balance:\t" + depositAccount.getMoneyBalance() + "\n");

    BankAccount creditAccount = new CreditAccount();
    creditAccount.depositMoney(5000);
    creditAccount.withdrawMoney(500);
    System.out.println("Credit account balance:\t" + creditAccount.getMoneyBalance() + "\n");
  }
}
