import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Company {

  private String companyName;
  private double companyIncome = 0;
  private HashMap<Integer, Employee> employees;
  private EmployeeComparator comparator = new EmployeeComparator();
  private Integer id;
  private static Integer idCounter = 0;

  Company(String companyName) {
    idCounter++;
    employees = new HashMap<>();
    this.companyName = companyName;
    id = idCounter;
  }

  void calcCompanyIncome() {
    employees.forEach((key, value) -> {
      if (value.getEmployeeType().equals("Manager")) {
        companyIncome = companyIncome + value.getManagerSales();
      }
    });
  }

  double getCompanyIncome() {
    return companyIncome;
  }

  String getCompanyIncomeToString() {
    return Helper.formatToRUB(companyIncome);
  }

  private Employee getEmployeeById(int id) {
    return employees.get(id);
  }

  void hireEmployee(Employee e) {
    employees.put(e.getId(), e);
    e.setCompany(this);
    System.out.println(
        e.getEmployeeType() +
            " id_" + e.getId() +
            " hired to " + companyName
    );
  }

  private List<Employee> getSalaryStaff() {
    List<Employee> salaryStaff = new ArrayList<>();
    for (Map.Entry entry : employees.entrySet()) {
      Employee e = (Employee) entry.getValue();
      salaryStaff.add(e);
    }
    return salaryStaff;
  }

  void getTopSalaryStaff(int count) {
    List<Employee> salaryStaff = getSalaryStaff();
    salaryStaff.sort(comparator);
    List<String> header = Arrays.asList(
        "Id",
        "Employee",
        "Highest salary"
    );

    Report report = createReport(
        "top salary staff",
        header,
        Report.generateSalaryStaffData(cropList(salaryStaff, count))
    );

    printReport(report);
  }

  void getLowestSalaryStaff(int count) {
    List<Employee> salaryStaff = getSalaryStaff();
    salaryStaff.sort(comparator.reversed());
    List<String> header = Arrays.asList(
        "Id",
        "Employee",
        "Lowest salary"
    );

    Report report = createReport(
        "lowest salary staff",
        header,
        Report.generateSalaryStaffData(cropList(salaryStaff, count))
    );

    printReport(report);
  }

  private List<Employee> cropList(List<Employee> list, int maxListSize) {
    if (list.size() >= maxListSize) {
      return list.subList(0, maxListSize);
    } else {
      return list;
    }
  }

  private Report createReport(String reportName, List<String> header, List<List<String>> data) {
    return new Report(
        reportName,
        header,
        data
    );
  }

  private void printReport(Report report) {
    System.out.println("\t\t" + report.getName().toUpperCase());
    TableGenerator table = new TableGenerator();
    System.out.println(table.generateTable(report.getHeader(), report.getDataList()));
  }

  public void dismissEmployee(Employee e) {
    e.setCompany(null);
    employees.remove(e.getId());
    System.out.println(
        "Employee id_" + e.getId() +
            " dismissed from " + companyName
    );
  }

  public void dismissAllEmployees() {
    employees.forEach((key, value) -> value.setCompany(null));
    employees.clear();
    System.out.println("All employees has been dismissed from " + companyName);
  }

  void dismissTenEmployee() {
    for (int i = 0; i < 10; i++) {
      Employee e;
      do {
        int randomId = Helper.randInt(1, employees.size());
        e = getEmployeeById(randomId);
      } while (e == null);
      e.setCompany(null);
      employees.remove(e.getId());
      System.out.println(
          "Random " + e.getEmployeeType() +
              " id_" + e.getId() +
              " dismissed from " + companyName
      );
    }
  }

  public String getEmployeeSalaryToString(double salary) {
    return Helper.formatToRUB(salary);
  }

  public Integer getId() {
    return id;
  }

  public String getCompanyName() {
    return this.companyName;
  }

  public HashMap<Integer, Employee> getEmployees() {
    return employees;
  }
}
