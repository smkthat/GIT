package Main.Java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Loader {

  private static ArrayList<Contact> contactsBook;
  private static String REGEX_PHONE = "^[0-9]+";
  private static boolean steelWorking = true;

  private static void fillTheBook() {
    contactsBook.add(new Contact("Me", "777777"));
    contactsBook.add(new Contact("Home", "111111"));
    contactsBook.add(new Contact("Work", "222222"));
    contactsBook.add(new Contact("Other", "000000"));
  }

  private static void runProgram() {
    fillTheBook(); // turn off if you want to get a clear phone book without entries

    System.err.println("<            Welcome to phonebook emulator :)            >");

    while (steelWorking) {
      System.out.println("\nYou have the following commands:   1 - print all contacts;\n"
          + "                                   2 - clear all contacts from;\n"
          + "                                   0 - stop program.\n"
      );
      System.out.println("\nPlease, type contact phone number/name or command:");
      String input = inputString();
      if (input.isEmpty()) {
        continue;
      }

      if (input.length() > 1) {
        ArrayList<Contact> foundedContacts = searchContacts(input);
        if (foundedContacts.isEmpty()) {
          createNewContact(input);
        } else {
          System.err.println("FOUNDED CONTACTS");
          System.out.println("Id:\tName:\tPhone:");

          for (Contact contact : foundedContacts) {
            System.out
                .println(contact.getId() + "\t" + contact.getName() + "\t" + contact.getPhone());
          }

          System.err.println("Do you want to change contact?");
          System.out.println("Please, enter contact id or print \"no\"!");

          while (true) {
            input = inputString();
            if (input.matches(REGEX_PHONE)) {
              Contact contact = getContactById(Integer.parseInt(input));
              if (contact != null) {
                changeContact(contact);
                break;
              } else {
                System.err.println("Wrong id! Repeat input:");
              }
            } else if (input.equals("n") || input.equals("no")) {
              break;
            }
          }

        }
      } else {

        switch (input) {
          case "1": {
            if (contactsBook.isEmpty()) {
              System.err.println("Phonebook empty! Nothing to print!");
            } else {
              printAllContacts();
            }
            continue;
          }
          case "2": {
            clearAllContacts();
            System.err.println("Phonebook cleared!");
            continue;
          }
          case "0": {
            steelWorking = false;
          }
        }
      }
    }
  }

  private static Contact getContactById(Integer id) {
    for (Contact contact : contactsBook) {
      if (contact.getId().equals(id)) {
        return contact;
      }
    }

    return null;
  }

  private static ArrayList<Contact> searchContacts(String contactInfo) {
    ArrayList<Contact> foundedContacts = new ArrayList<>();
    for (Contact contact : contactsBook) {
      if (contact.getPhone().equals(contactInfo) || contact.getName().equals(contactInfo)) {
        foundedContacts.add(contact);
      }
    }

    Set<Contact> set = new HashSet<>(foundedContacts);
    foundedContacts.clear();
    foundedContacts.addAll(set);
    return foundedContacts;
  }

  private static String inputString() {
    String input = "";
    while (input.isEmpty()) {
      input = new Scanner(System.in).nextLine();

      if (input.isEmpty()) {
        System.err.println("Please, repeat input:");
      }
    }

    return input;
  }

  private static void printAllContacts() {
    System.err.println("PHONEBOOK CONTACTS");
    System.out.println("Name:\tPhone:");
    for (Contact contact : contactsBook) {
      System.out.println(contact.getName() + "\t" + contact.getPhone());
    }
  }

  private static void clearAllContacts() {
    contactsBook.clear();
  }

  private static void createNewContact(String input) {
    String name, phone;
    Contact newContact;

    System.err.println("Contact not found!");
    System.out.println("Create new contact for " + input + "?\nPlease, type answer\n"
        + "y - yes;\n"
        + "n - no.\n"
        + "Input: "
    );

    String answer = inputString();
    if (answer.equals("y") || answer.equals("yes")) {
      if (input.matches(REGEX_PHONE)) {
        System.out.println("Please, type contact name:");
        name = inputString();
        newContact = new Contact(name, input);
      } else {
        System.out.println("Please, type contact phone:");
        phone = inputString();
        newContact = new Contact(input, phone);
      }
      contactsBook.add(newContact);
      System.err.println(
          "New contact " + newContact.getName() + "\t" + newContact.getPhone() + " created!");
    }
  }

  private static void changeContact(Contact contact) {
    System.out.println(
        "\nIf you want to change contact \""
            + contact.getName()
            + "\" ?\n"
            + "please enter the available command:    1 - do not change;\n"
            + "                                       2 - change phone number;\n"
            + "                                       3 - rename contact;\n"
            + "                                       4 - remove contact from phonebook;\n"
            + "                                       0 - stop program.\n"
    );

    String command = "";
    while (!command.matches("^[0-4]$")) {
      command = inputString();
    }

    switch (command) {
      case "0": {
        steelWorking = false;
        break;
      }
      case "1": {
        break;
      }
      case "2": {
        System.err.println("Please, enter new contact phone number:");
        String phone = "";
        while (!phone.matches(REGEX_PHONE)) {
          phone = inputString();
        }
        contact.setPhone(phone);
        System.err.println(
            "Contact " + contact.getName() + " change phone number to " + contact.getPhone());
        break;
      }
      case "3": {
        System.err.println("Please, enter new contact name:");
        String oldName = contact.getName();
        String name = "";
        while (name.isEmpty()) {
          name = inputString();
        }
        contact.setName(name);
        System.err.println("Contact " + oldName + " change name to " + contact.getName());
        break;
      }
      case "4": {
        System.err.println("Contact " + contact.getName() + " removed!");
        contactsBook.remove(contact);
        break;
      }
    }
  }

  public static void main(String[] args) {
    contactsBook = new ArrayList<>();
    runProgram();
  }
}
