package Main.Java;

import Main.Java.Accounts.DefaultAccount;
import Main.Java.Accounts.CreditAccount;
import Main.Java.Accounts.DepositoryAccount;
import Main.Java.Clients.Businessman;
import Main.Java.Clients.EntityClient;
import Main.Java.Clients.IndividualClient;
import java.time.LocalDate;

public class Loader {

  public static void main(String[] args) {
    System.err.println("Check Individual client:");
    IndividualClient individualClient = new IndividualClient();
    System.err.println("Default account");
    DefaultAccount account = new DefaultAccount();
    individualClient.setAccount(account);
    individualClient.depositMoney(1000);
    individualClient.getBalance();

    System.err.println("Depository account");
    account = new DepositoryAccount();
    individualClient.setAccount(account);
    individualClient.depositMoney(1000);
    individualClient.withdrawMoney(500);
    individualClient.getBalance();

    System.err.println("Credit account");
    account = new CreditAccount();
    individualClient.setAccount(account);
    individualClient.depositMoney(10000);
    individualClient.withdrawMoney(1000);
    individualClient.getBalance();

    System.err.println("\nCheck Entity client:");
    EntityClient entityClient = new EntityClient();
    System.err.println("Default account");
    account = new DefaultAccount();
    entityClient.setAccount(account);
    entityClient.depositMoney(150000);
    entityClient.withdrawMoney(1000);
    entityClient.getBalance();

    System.err.println("Depository account");
    account = new DepositoryAccount();
    entityClient.setAccount(account);
    entityClient.depositMoney(150000);
    entityClient.withdrawMoney(1000);
    entityClient.getBalance();

    System.err.println("Credit account");
    account = new CreditAccount();
    entityClient.setAccount(account);
    entityClient.depositMoney(1000000);
    entityClient.withdrawMoney(10000);
    entityClient.getBalance();

    System.err.println("\nCheck Businessman:");
    Businessman businessman = new Businessman();
    System.err.println("Default account");
    account = new DefaultAccount();
    businessman.setAccount(account);
    businessman.depositMoney(150000);
    businessman.withdrawMoney(1000);
    businessman.getBalance();

    System.err.println("Depository account");
    account = new DepositoryAccount();
    businessman.setAccount(account);
    businessman.depositMoney(150000);
    businessman.withdrawMoney(1000);
    businessman.getBalance();

    System.err.println("Credit account");
    account = new CreditAccount();
    businessman.setAccount(account);
    businessman.depositMoney(1000000);
    businessman.withdrawMoney(10000);
    businessman.getBalance();
  }
}
