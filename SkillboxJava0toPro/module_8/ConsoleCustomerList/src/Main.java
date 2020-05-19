import java.util.Scanner;

public class Main {

  private static String addCommand = "add Василий Петров " +
      "vasily.petrov@gmail.com +79215637722";
  private static String commandExamples = "\t" + addCommand + "\n" +
      "\tlist\n\tcount\n\tremove Василий Петров\n\texit";
  private static String commandError = "Wrong command! Available command examples: \n" +
      commandExamples;
  private static String helpText = "Command examples:\n" + commandExamples;
  private static boolean isWorking = true;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    CustomerStorage executor = new CustomerStorage();

    while (isWorking) {
      String command = scanner.nextLine();
      String[] tokens = command.split("\\s+", 2);
      try {
        switch (tokens[0]) {
          case "add":
            if (tokens.length != 2) {
              throw new IllegalArgumentException("Wrong add data input!");
            } else {
              executor.addCustomer(tokens[1]);
            }
            break;
          case "list":
            executor.listCustomers();
            break;
          case "remove":
            if (tokens.length != 2) {
              throw new IllegalArgumentException("Wrong remove data input!");
            } else {
              executor.removeCustomer(tokens[1]);
            }
            break;
          case "count":
            System.out.println("There are " + executor.getCount() + " customers");
            break;
          case "help":
            System.out.println(helpText);
            break;
          case "exit":
            isWorking = false;
            break;
          default:
            System.out.println(commandError);
            break;
        }
      } catch (IllegalArgumentException ex) {
        ex.printStackTrace();
      }
    }
  }
}
