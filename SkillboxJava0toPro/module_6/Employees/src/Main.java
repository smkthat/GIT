import java.util.List;

public class Main {

  public static void main(String[] args) {
    Company company = new Company("\"JDCompany®\"");
    generateAndHireEmployees(company, 20, 50, 200);
    company.dismissTenEmployee();
    company.getTopSalaryStaff(10);
    System.out.println(company.getCompanyIncomeToString());
  }

  private static void generateAndHireEmployees(Company company, int topsCount, int clerksCount,
      int managersCount) {
    generateTopManagers(company, topsCount);
    generateClerks(company, clerksCount);
    generateManagers(company, managersCount);
  }

  private static void generateManagers(Company company, int count) {
    Employee e;
    for (int i = 0; i < count; i++) {
      e = new Manager();
      company.hireEmployee(e);
      e.sale(Helper.randInt(1, 100) * 1000);
    }
  }

  private static void generateClerks(Company company, int count) {
    Employee e;
    for (int i = 0; i < count; i++) {
      e = new Clerk();
      company.hireEmployee(e);
    }
  }

  private static void generateTopManagers(Company company, int count) {
    Employee e;
    for (int i = 0; i < count; i++) {
      e = new TopManager();
      company.hireEmployee(e);
    }
  }
}
