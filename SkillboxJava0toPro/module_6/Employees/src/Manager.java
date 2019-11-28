class Manager extends Employee {

  private static final double MANAGER_SALE_PERCENT = 0.05;

  Manager() {
    super(40000);
  }

  @Override
  public double getMonthSalary() {
    return getManagerSales() * MANAGER_SALE_PERCENT + getFixSalary();
  }
}
