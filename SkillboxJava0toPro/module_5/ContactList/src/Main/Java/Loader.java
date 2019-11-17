package Main.Java;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Loader {

  private static TreeMap<String, String> phoneBook = new TreeMap<>();
  private static String REGEX_PHONE = "^[0-9]+";
  private static boolean steelWorking = true;

  private static void fillTheBook() {
    phoneBook.put("88005553535", "Me");
    phoneBook.put("655050", "Home");
    phoneBook.put("621154", "Office");
    phoneBook.put("627595", "Chif");
  }

  private static void runProgram() {
    fillTheBook(); // turn off if you want to get a clear phone book without entries

    System.out.println("            Welcome to phonebook emulator :)\n\n"
        + "You have the following commands:   1 - print all contacts;\n"
        + "                                   2 - clear all contacts from;\n"
        + "                                   0 - stop program.\n"
    );

    while (steelWorking) {
      System.out.println("\nPlease, type contact phone number/name or command:");
      String input = inputString();
      if (input.isEmpty()) {
        continue;
      }

      if (input.length() > 1) {
        String contactName = "";
        String contactPhone = "";
        String contactInfo;
        boolean isNewContact = false;

        if (input.matches(REGEX_PHONE)) {
          contactInfo = getContactName(input);
          if (contactInfo == null) {
            isNewContact = true;
            createNewContact(input);
          } else {
            contactPhone = input;
            contactName = contactInfo;
            System.err.println(contactName);
          }
        } else {
          contactInfo = getContactPhone(input);

          if (contactInfo == null) {
            isNewContact = true;
            createNewContact(input);
            continue;
          } else {
            contactPhone = contactInfo;
            contactName = input;
            System.err.println(contactPhone);
          }
        }

        if (!isNewContact) {
          System.out.println("Contact found!");
          changeContact(contactName, contactPhone);
        }
      } else {

        switch (input) {
          case "1": {
            if (phoneBook.isEmpty()) {
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

  private static String getContactPhone(String contactInfo) {
    for (Map.Entry entry : phoneBook.entrySet()) {
      if (entry.getValue().equals(contactInfo)) {
        return (String) entry.getKey();
      }
    }
    return null;
  }

  private static String getContactName(String phone) {
    return phoneBook.get(phone);
  }

  private static void printAllContacts() {
    for (Map.Entry entry : phoneBook.entrySet()) {
      System.out.println(entry.getValue() + "\t" + entry.getKey());
    }
  }

  private static void clearAllContacts() {
    phoneBook.clear();
  }

  private static void createNewContact(String input) {
    String name, phone;

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
        phone = input;
      } else {
        System.out.println("Please, type contact phone:");
        phone = inputString();
        name = input;
      }
      phoneBook.put(phone, name);
      System.err.println("New contact " + name + "\t" + phone + " created!");
    }
  }

  private static void changeContact(String name, String phone) {
    String key = phone;
    System.out.println(
        "\nIf you want to change contact \""
            + name
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
        phone = "";
        while (!phone.matches(REGEX_PHONE)) {
          phone = inputString();
        }
        updateContactInfo(key, name, phone);
        System.err.println("Contact " + name + " change phone number to " + phone);
        break;
      }
      case "3": {
        System.err.println("Please, enter new contact name:");
        String oldName = name;
        name = "";
        while (name.isEmpty()) {
          name = inputString();
        }
        updateContactInfo(key, name, phone);
        System.err.println("Contact " + oldName + " change name to " + name);
        break;
      }
      case "4": {
        removeContact(key);
        System.err.println("Contact " + name + " removed!");
        break;
      }
    }
  }

  private static void updateContactInfo(String key, String contactName, String contactPhone) {
    removeContact(key);
    phoneBook.put(contactPhone, contactName);
  }

  private static void removeContact(String key) {
    phoneBook.remove(key);
  }

  public static void main(String[] args) {
    runProgram();
  }
}
