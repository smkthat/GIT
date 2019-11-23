public class TopManager extends Employee implements EmployeeInterface {

  private static final double TOP_MANAGER_SALARY_BONUS = 50000;

  TopManager() {
    super("TopManager", 50000);
  }

  @Override
  public double getMonthSalary() {
    if (getCompany().getCompanyIncome() > 10000000) {
      return getFixSalary() + TOP_MANAGER_SALARY_BONUS;
    } else {
      return getFixSalary();
    }
  }
}
