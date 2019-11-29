public class Main {

  public static void main(String[] args) {
    Company jdCompany = new Company("\"JDCompany®\"");
    generateAndHireEmployees(jdCompany, 10, 180, 80);
    jdCompany.fireRandom(10);
    jdCompany.getTopSalaryStaff(15);
    jdCompany.getLowestSalaryStaff(10);
    jdCompany.getMaxSalaryStaffForHireDateYear(2017);
    System.out.println(jdCompany.getIncomeToString());
    System.out.println(
        jdCompany.getName() + " staff size: " + jdCompany.getEmployees().size() + " employees");

    System.out.println("\n" +
        "--------------------------------------------------------------"
    );

    Company abCraft = new Company("\"ABCraft®\"");
    generateAndHireEmployees(abCraft, 15, 185, 100);
    abCraft.fireRandom(100);
    abCraft.getTopSalaryStaff(20);
    abCraft.getLowestSalaryStaff(5);
    abCraft.getMaxSalaryStaffForHireDateYear(2017);
    System.out.println(abCraft.getIncomeToString());
    System.out.println(
        abCraft.getName() + " staff size: " + abCraft.getEmployees().size() + " employees");
  }

  private static void generateAndHireEmployees(Company company, int topsCount, int operatorsCount,
      int managersCount) {
    generateTopManagers(company, topsCount);
    generateOperators(company, operatorsCount);
    generateManagers(company, managersCount);
  }

  private static void generateManagers(Company company, int count) {
    for (int i = 0; i < count; i++) {
      Employee e = new Manager();
      company.hireEmployee(e);
      e.sale(Helper.randInt(1, 5) * 1000);
    }
  }

  private static void generateOperators(Company company, int count) {
    for (int i = 0; i < count; i++) {
      Employee e = new Operator();
      company.hireEmployee(e);
    }
  }

  private static void generateTopManagers(Company company, int count) {
    for (int i = 0; i < count; i++) {
      Employee e = new TopManager();
      company.hireEmployee(e);
    }
  }
}
