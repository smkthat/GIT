import java.util.Comparator;

public class Employee {

  private Integer id;
  private String employeeType;
  private double fixSalary;
  private static Integer idCounter = 0;
  private double managerSales;
  private Company company;
  private double monthSalary;

  Employee(String employeeType, double fixSalary) {
    idCounter++;
    this.employeeType = employeeType;
    this.fixSalary = fixSalary;
    id = idCounter;
  }

  public void setMonthSalary(double monthSalary) {
    this.monthSalary = monthSalary;
  }

  public double getMonthSalary() {
    return monthSalary;
  }

  void sale(double saleAmount) {
    managerSales = managerSales + saleAmount;
    System.out.println("The manager earned the company " + saleAmount);
    company.calcCompanyIncome();
  }

  double getManagerSales() {
    return managerSales;
  }

  String getEmployeeType() {
    return employeeType;
  }

  double getFixSalary() {
    return fixSalary;
  }

  public void setFixSalary(double fixSalary) {
    this.fixSalary = fixSalary;
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
