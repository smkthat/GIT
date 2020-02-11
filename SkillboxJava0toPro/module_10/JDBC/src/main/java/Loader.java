public class Loader {

  public static void main(String[] args) {

//    runJDBC();
    runHibernate();

    System.out.println("Goodbye :)");
  }

  private static void runHibernate() {
    createLinkedPurchaseTableByJDBC();
    try (Hiber hiber = new Hiber()) {
      hiber.fillLinkedPurchaseListTable();
      hiber.printLinkedPurchaseListTable();

//      hiber.printCourseTable();
//      hiber.printStudentsTable();
//      hiber.printTeachersTable();
//      hiber.printSubscriptionsTable();
//      hiber.printPurchaseListTable();
//      hiber.printDBTable("subscriptions");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void createLinkedPurchaseTableByJDBC() {
    try (JDBC jdbc = new JDBC()) {
      jdbc.executeScript("linked_purchase_list_creation_script.sql");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void runJDBC() {
    try (JDBC jdbc = new JDBC()) {

      jdbc.printTableFromQuery(
          "SELECT course_name entitys.Course, "
              + "COUNT(subscription_date) / TIMESTAMPDIFF(MONTH,MIN(subscription_date),MAX(subscription_date)) \"Avg subscription per month\" "
              + "FROM purchaselist "
              + "GROUP BY course_name;");

      jdbc.printTableFromQuery(
          "SELECT c.name entitys.Course, "
              + "COUNT(s.subscription_date) / TIMESTAMPDIFF(MONTH,MIN(s.subscription_date),MAX(s.subscription_date)) \"Avg subscription per month\" "
              + "FROM courses c "
              + "JOIN subscriptions s ON c.id=s.course_id "
              + "GROUP BY c.name;");

      jdbc.printTableFromQuery(
          "SELECT c.name course,c.type, t.name teacher "
              + "FROM courses c "
              + "JOIN teachers t ON c.teacher_id=t.id "
              + "ORDER BY teacher");

      jdbc.printTableFromQuery("SHOW TABLES;");

      jdbc.printTableFromQuery("SELECT * FROM teachers;");

      jdbc.printTableFromQuery("DESCRIBE Courses;");

      jdbc.printTableFromQuery("UPDATE");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
