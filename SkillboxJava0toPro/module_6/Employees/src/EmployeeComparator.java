import java.util.Comparator;

public class EmployeeComparator implements Comparator<Employee> {

  @Override
  public int compare(Employee a, Employee b) {
    return Double.compare(a.getMonthSalary(), b.getMonthSalary());
  }
}
