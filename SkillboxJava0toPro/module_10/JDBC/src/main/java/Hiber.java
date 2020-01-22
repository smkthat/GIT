import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hiber implements AutoCloseable {

  private SessionFactory sessionFactory;

  @Override
  public void close() {
    sessionFactory.close();
  }

  Hiber() {
    sessionFactory =
        new MetadataSources(
                new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build())
            .getMetadataBuilder()
            .build()
            .getSessionFactoryBuilder()
            .build();
  }

  public void printDBTable(String tableName) {
    System.out.println(
        new TableGenerator(getColumnsNameFromDBTable(tableName), getRowsFromDBTable(tableName))
            .getResult());
  }

  private List<String> getColumnsNameFromDBTable(String tableName) {
    String query = "SHOW FIELDS FROM " + tableName + ";";
    try (Session session = sessionFactory.openSession()) {
      List<String> headList = new ArrayList<>();
      for (Object[] column : (List<Object[]>) session.createSQLQuery(query).list()) {
        headList.add(column[0].toString());
      }
      return headList;
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  private List<List<String>> getRowsFromDBTable(String tableName) {
    String query = "SELECT * FROM " + tableName + ";";
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> dataList = new ArrayList<>();
      for (Object[] row : (List<Object[]>) session.createSQLQuery(query).list()) {
        List<String> rowList = new ArrayList<>();
        for (int i = 0; i < row.length; i++) {
          if (row[i] == null) {
            row[i] = "";
          }
          rowList.add(row[i].toString());
        }
        dataList.add(rowList);
      }

      return dataList;
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printCourseTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (Object c : session.createQuery("from Course").list()) {
        tableData.add(getCourseDataForTable((Course) c));
      }

      System.out.println(new TableGenerator(getCourseHeadForTable(), tableData).getResult());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printStudentsTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (Object student : session.createQuery("from Student").list()) {
        tableData.add(getStudentsDataForTable((Student) student));
      }

      System.out.println(new TableGenerator(getStudentHeadForTable(), tableData).getResult());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printTeachersTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (Object teacher : session.createQuery("from Teacher").list()) {
        tableData.add(getTeacherDataForTable((Teacher) teacher));
      }

      System.out.println(new TableGenerator(getTeacherHeadForTable(), tableData).getResult());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printSubscriptionsTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (Object subscription : session.createQuery("from Subscription").list()) {
        tableData.add(getSubscriptionDataForTable((Subscription) subscription));
      }

      System.out.println(new TableGenerator(getSubscriptionHeadForTable(), tableData).getResult());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> getSubscriptionDataForTable(Subscription s) {
    return Arrays.asList(
            nullReplacer(s.getStudent().getId()),
            nullReplacer(s.getCourse().getId()),
            nullReplacer(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(s.getSubscriptionDate())));
  }

  private List<String> getStudentsDataForTable(Student s) {
    return Arrays.asList(
        nullReplacer(s.getId()),
        nullReplacer(s.getName()),
        nullReplacer(s.getAge()),
        nullReplacer(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(s.getRegistrationDate())));
  }

  private List<String> getCourseDataForTable(Course c) {
    return Arrays.asList(
        nullReplacer(c.getId()),
        nullReplacer(c.getName()),
        nullReplacer(c.getDuration()),
        nullReplacer(c.getType()),
        nullReplacer(c.getDescription()),
        nullReplacer(c.getTeacherId()),
        nullReplacer(c.getStudentsCount()),
        nullReplacer(c.getPrice()),
        nullReplacer(c.getPricePerHour()));
  }

  private List<String> getTeacherDataForTable(Teacher t) {
    return Arrays.asList(
        nullReplacer(t.getId()),
        nullReplacer(t.getName()),
        nullReplacer(t.getSalary()),
        nullReplacer(t.getAge()));
  }

  private List<String> getCourseHeadForTable() {
    return Arrays.asList(
        "id",
        "name",
        "duration",
        "type",
        "description",
        "teacherId",
        "studentsCount",
        "price",
        "pricePerHour");
  }

  private List<String> getStudentHeadForTable() {
    return Arrays.asList("id", "name", "age", "registration_date");
  }

  private List<String> getTeacherHeadForTable() {
    return Arrays.asList("id", "name", "salary", "age");
  }

  private List<String> getSubscriptionHeadForTable() {
    return Arrays.asList("student_id", "course_id", "subscription_date");
  }

  private String nullReplacer(Object o) {
    if (o == null) {
      return "";
    } else {
      return o.toString();
    }
  }
}
