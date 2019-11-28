import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Company {

  private static Integer idCounter = 0;
  private String name;
  private double companyIncome = 0;
  private HashMap<Integer, Employee> employees;
  private EmployeeComparator comparator = new EmployeeComparator();
  private Integer id;
  private List<Report> reportsList;

  Company(String name) {
    idCounter++;
    employees = new HashMap<>();
    reportsList = new LinkedList<>();
    this.name = name;
    id = idCounter;
  }

  void calcIncome() {
    employees.forEach((key, value) -> {
      if (value.getEmployeeType().equals("Manager")) {
        companyIncome = companyIncome + value.getManagerSales();
      }
    });
  }

  double getIncome() {
    return companyIncome;
  }

  String getIncomeToString() {
    return name + " income:\t" + Helper.formatToRUB(companyIncome);
  }

  private Employee getEmployeeById(int id) {
    return employees.get(id);
  }

  void hireEmployee(Employee e) {
    employees.put(e.getId(), e);
    e.setCompany(this);
//    System.out.println(
//        e.getEmployeeType() +
//            " id_" + e.getId() +
//            " hired to " + name
//    );
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
    salaryStaff.sort(comparator.reversed());
    List<String> header = Arrays.asList(
        "N",
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
    salaryStaff.sort(comparator);
    List<String> header = Arrays.asList(
        "N",
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
    Report report = new Report(
        reportName,
        header,
        data
    );
    reportsList.add(report);
    return report;
  }

  private void printReport(Report report) {
    TableGenerator generator = new TableGenerator();
    String table = generator.generateTable(report.getHeader(), report.getDataList());
    int tabsCount = Math.round((table.substring(0, table.indexOf("\n")).length() / 4) / 2);
    System.out.println("\n" + Helper.getTAB(tabsCount + 2) + getName());
    System.out.println(Helper.getTAB(tabsCount) + report.getName().toUpperCase());
    System.out.println(table);
  }

  public void fire(Employee e) {
    e.setCompany(null);
    employees.remove(e.getId());
//    System.out.println(
//        "Employee id_" + e.getId() +
//            " dismissed from " + name
//    );
  }

  public void fireAll() {
    employees.forEach((key, value) -> value.setCompany(null));
    employees.clear();
    System.out.println("All employees has been dismissed from " + name);
  }

  void fireRandom(int count) {
    for (int i = 0; i < count; i++) {
      Random random = new Random();
      List<Integer> keys = new ArrayList<>(employees.keySet());
      Integer randomKey = keys.get(random.nextInt(keys.size()));
      Employee e = employees.get(randomKey);
      fire(e);
    }
  }

  String getEmployeeSalaryToString(double salary) {
    return Helper.formatToRUB(salary);
  }

  Integer getId() {
    return id;
  }

  String getName() {
    return this.name;
  }

  HashMap<Integer, Employee> getEmployees() {
    return employees;
  }

  public List<Report> getReportsList() {
    return reportsList;
  }

  public void setReportsList(List<Report> reportsList) {
    this.reportsList = reportsList;
  }

  public Report getReport(int index) {
    return reportsList.get(index);
  }

  public void addReport(Report report) {
    reportsList.add(report);
  }

  public void removeReport(int index) {
    reportsList.remove(index);
  }
}
