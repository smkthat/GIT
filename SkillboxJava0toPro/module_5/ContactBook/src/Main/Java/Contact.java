package Main.Java;

class Contact {
  private static Integer idCounter = 1;
  private Integer id;
  private String name;
  private String phone;

  Contact(String name, String phone) {
    this.id = idCounter++;
    this.name = name;
    this.phone = phone;
  }

  Integer getId() {
    return id;
  }

  String getName() {
    return name;
  }

  void setName(String name) {
    this.name = name;
  }

  String getPhone() {
    return phone;
  }

  void setPhone(String phone) {
    this.phone = phone;
  }
}
