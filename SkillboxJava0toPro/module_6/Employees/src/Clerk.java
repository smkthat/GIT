public class Clerk extends Employee implements EmployeeInterface {

  Clerk() {
    super("Clerk", 40000);
  }

  @Override
  public double getMonthSalary() {
    return getFixSalary();
  }
}
