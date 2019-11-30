import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Company {

  private static Integer idCounter = 0;
  private Integer id;
  private String name;
  private double companyIncome = 0;
  private List<Report> reportsList;
  private HashMap<Integer, Employee> employees = new HashMap<>();

  Company(String name) {
    reportsList = new LinkedList<>();
    this.name = name;
    id = ++idCounter;
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

  void hireEmployee(Employee e) {
    employees.put(e.getId(), e);
    e.setCompany(this);
    e.setHireDate(Helper.generateRandomDate(2017, 2019));
  }

  @Override
  public String toString() {
    return id.toString() + "\t" + name + "\t" + employees.size() + "\t" + getIncomeToString();
  }

  private List<Employee> getSalaryStaff() {
    return new ArrayList<>(employees.values());
  }

  void getTopSalaryStaff(int count) {
    List<Employee> salaryStaff = getSalaryStaff();
    salaryStaff.sort(
        Comparator
            .comparingDouble(Employee::getMonthSalary)
            .reversed()
            .thenComparing(Employee::toString)
    );

    List<String> header = Arrays.asList(
        "N",
        "Id",
        "Employee",
        "Highest salary"
    );

    var report = createReport(
        "top salary staff",
        header,
        Report.generateSalaryStaffData(cropList(salaryStaff, count))
    );

    printReport(report);
    reportsList.add(report);
  }

  void getLowestSalaryStaff(int count) {
    List<Employee> salaryStaff = getSalaryStaff();
    salaryStaff.sort(
        Comparator
            .comparingDouble(Employee::getMonthSalary)
            .thenComparing(Employee::toString)
    );

    List<String> header = Arrays.asList(
        "N",
        "Id",
        "Employee",
        "Lowest salary"
    );

    var report = createReport(
        "lowest salary staff",
        header,
        Report.generateSalaryStaffData(cropList(salaryStaff, count))
    );
    printReport(report);
    reportsList.add(report);
  }

  void getMaxSalaryStaffForHireDateYear(int year) {
    var salaryStaff = getSalaryStaff();
    salaryStaff = salaryStaff
        .stream()
        .filter(e -> e.getHireDate().getYear() == year)
        .collect(Collectors.toList());

    salaryStaff.sort(
        Comparator
            .comparing(Employee::getMonthSalary)
            .reversed()
    );

    List<String> header = Arrays.asList(
        "N",
        "Id",
        "Hire date",
        "Employee",
        "Highest salary"
    );

    var report = createReport(
        "max salary staff hired in " + year,
        header,
        Report.generateMaxSalaryStaffDataForHireDate(salaryStaff)
    );

    printReport(report);
    reportsList.add(report);
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
    TableGenerator generator = new TableGenerator();
    String table = generator.generateTable(report.getHeader(), report.getDataList());
    int tabsCount = Math.round((table.substring(0, table.indexOf("\n")).length() / 4.0f) / 2.0f);
    System.out.println("\n" + Helper.getTAB(tabsCount + 2) + getName());
    System.out.println(Helper.getTAB(tabsCount) + report.getName().toUpperCase());
    System.out.println(table);
  }

  private void fire(Employee e) {
    e.setCompany(null);
    employees.remove(e.getId());
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

  String getName() {
    return this.name;
  }

  HashMap<Integer, Employee> getEmployees() {
    return employees;
  }

  public Integer getId() {
    return id;
  }

  public List<Report> getReportsList() {
    return reportsList;
  }
}
