import java.util.HashMap;

class CustomerStorage {

  private static final String VALID_PHONE =
      "^\\+[0-9]{11}";
  private static final String VALID_EMAIL =
      "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  private HashMap<String, Customer> storage;

  CustomerStorage() {
    storage = new HashMap<>();
  }

  void addCustomer(String data) {
    String[] components = data.split("\\s+");
    if (components.length != 4) {
      throw new IllegalArgumentException("Wrong input!");
    } else if (!isValidEmail(components[2])) {
      throw new IllegalArgumentException("Invalid input email!");
    } else if (!isValidPhone(components[3])) {
      throw new IllegalArgumentException("Invalid input phone!");
    } else {
      String name = components[0] + " " + components[1];
      storage.put(name, new Customer(name, components[3], components[2]));
    }
  }

  private boolean isValidEmail(String email) {
    return email.matches(VALID_EMAIL);
  }

  private boolean isValidPhone(String phone) {
    return phone.replaceAll("[()\\-\\s]+", "").matches(VALID_PHONE);
  }

  void listCustomers() {
    storage.values().forEach(System.out::println);
  }

  void removeCustomer(String data) {
    String[] components = data.split("\\s+");
    if (components.length != 2) {
      throw new IllegalArgumentException("Wrong name!");
    } else {
      String name = components[0] + " " + components[1];
      storage.remove(name);
    }
  }

  int getCount() {
    return storage.size();
  }
}