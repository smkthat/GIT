public abstract class Employee {

  private Integer id;
  private double fixSalary;
  private static Integer idCounter = 0;
  private double managerSales;
  private Company company;

  Employee(double fixSalary) {
    idCounter++;
    this.fixSalary = fixSalary;
    id = idCounter;
  }

  public double getMonthSalary() {
    return getFixSalary();
  }

  void sale(double saleAmount) {
    managerSales = managerSales + saleAmount;
    //System.out.println("The manager earned to company " + saleAmount);
    company.calcIncome();
  }

  double getManagerSales() {
    return managerSales;
  }

  String getEmployeeType() {
    return this.getClass().getSimpleName();
  }

  double getFixSalary() {
    return fixSalary;
  }

  Company getCompany() {
    return company;
  }

  void setCompany(Company company) {
    this.company = company;
  }

  Integer getId() {
    return id;
  }
}
