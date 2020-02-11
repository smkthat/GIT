import entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

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

  public void fillLinkedPurchaseListTable() {
    try (Session session = sessionFactory.openSession()) {
      List<LinkedPurchaseList> linkedPurchaseLists =
          session
              .createQuery("from entities.LinkedPurchaseList", LinkedPurchaseList.class)
              .getResultList();

      List<PurchaseList> purchaseLists =
          session.createQuery("from entities.PurchaseList", PurchaseList.class).getResultList();

      Transaction transaction = session.beginTransaction();

      for (PurchaseList p : purchaseLists) {
        Student student = (Student) findByField("name", p.getStudentName(), Student.class, session);
        Course course = (Course) findByField("name", p.getCourseName(), Course.class, session);

        LinkedPurchaseList lpl =
            new LinkedPurchaseList(
                new LinkedPurchaseList.Id(student.getId(), course.getId()),
                student,
                course,
                course.getPrice(),
                p.getSubscriptionDate());

        if (!isFoundedLPL(linkedPurchaseLists, lpl)) {
          session.save(lpl);
        }
      }

      transaction.commit();
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean isFoundedLPL(List<LinkedPurchaseList> list, LinkedPurchaseList lpl) {
    return list.stream()
        .anyMatch(
            p ->
                p.getStudent().getId().equals(lpl.getStudent().getId())
                    && p.getCourse().getId().equals(lpl.getCourse().getId())
                    && p.getSubscriptionDate().equals(lpl.getSubscriptionDate()));
  }

  private Object findByField(String field, String value, Class c, Session session) {
    return DetachedCriteria.forClass(c)
        .add(Restrictions.eq(field, value))
        .getExecutableCriteria(session)
        .uniqueResult();
  }

  public void printDBTable(String tableName) {
    System.out.println(tableName.toUpperCase() + " TABLE");
    System.out.println(
        getTableToString(getColumnsNameFromDBTable(tableName), getRowsFromDBTable(tableName)));
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

      for (Course c : session.createQuery("from entities.Course", Course.class).list()) {
        tableData.add(getCourseDataForTable(c));
      }

      System.out.println("Course table".toUpperCase());
      System.out.println(getTableToString(getCourseHeadForTable(), tableData));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printStudentsTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (Student student : session.createQuery("from entities.Student", Student.class).list()) {
        tableData.add(getStudentsDataForTable(student));
      }

      System.out.println("Students table".toUpperCase());
      System.out.println(getTableToString(getStudentHeadForTable(), tableData));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printTeachersTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (Teacher teacher : session.createQuery("from entities.Teacher", Teacher.class).list()) {
        tableData.add(getTeacherDataForTable(teacher));
      }

      System.out.println("Teachers table".toUpperCase());
      System.out.println(getTableToString(getTeacherHeadForTable(), tableData));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printSubscriptionsTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (Subscription subscription :
          session.createQuery("from entities.Subscription", Subscription.class).list()) {
        tableData.add(getSubscriptionDataForTable(subscription));
      }

      System.out.println("Subscriptions table".toUpperCase());
      System.out.println(getTableToString(getSubscriptionHeadForTable(), tableData));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printPurchaseListTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (PurchaseList pl :
          session.createQuery("from entities.PurchaseList", PurchaseList.class).list()) {
        tableData.add(getPurchaseListDataForTable(pl));
      }

      System.out.println("PurchaseList table".toUpperCase());
      System.out.println(getTableToString(getPurchaseListHeadForTable(), tableData));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public void printLinkedPurchaseListTable() {
    try (Session session = sessionFactory.openSession()) {
      List<List<String>> tableData = new ArrayList<>();

      for (LinkedPurchaseList lpl :
          session
              .createQuery("from entities.LinkedPurchaseList", LinkedPurchaseList.class)
              .list()) {
        tableData.add(getLinkedPurchaseListDataForTable(lpl));
      }

      System.out.println("LinkedPurchaseList table".toUpperCase());
      System.out.println(getTableToString(getLinkedPurchaseListHeadForTable(), tableData));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  private String getTableToString(List<String> head, List<List<String>> data) {
    return new TableGenerator(head, data).getResult();
  }

  private List<String> getSubscriptionDataForTable(Subscription s) {
    return Arrays.asList(
        nullReplacer(s.getStudent().getName()),
        nullReplacer(s.getCourse().getName()),
        nullReplacer(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(s.getSubscriptionDate())));
  }

  private List<String> getStudentsDataForTable(Student s) {
    return Arrays.asList(
        nullReplacer(s.getId()),
        nullReplacer(s.getName()),
        nullReplacer(s.getAge()),
        nullReplacer(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(s.getRegistrationDate())));
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

  private List<String> getPurchaseListDataForTable(PurchaseList pl) {
    return Arrays.asList(
        nullReplacer(pl.getStudentName()),
        nullReplacer(pl.getCourseName()),
        nullReplacer(pl.getPrice()),
        nullReplacer(pl.getSubscriptionDate()));
  }

  private List<String> getLinkedPurchaseListDataForTable(LinkedPurchaseList lpl) {
    return Arrays.asList(
        nullReplacer(lpl.getStudent().getId()),
        nullReplacer(lpl.getCourse().getId()),
        nullReplacer(lpl.getPrice()),
        nullReplacer(lpl.getSubscriptionDate()));
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

  private List<String> getPurchaseListHeadForTable() {
    return Arrays.asList("student_name", "course_name", "price", "subscription_date");
  }

  private List<String> getLinkedPurchaseListHeadForTable() {
    return Arrays.asList("student_id", "course_id", "price", "subscription_date");
  }

  private List<String> getStudentHeadForTable() {
    return Arrays.asList("id", "name", "age", "registration_date");
  }

  private List<String> getTeacherHeadForTable() {
    return Arrays.asList("id", "name", "salary", "age");
  }

  private List<String> getSubscriptionHeadForTable() {
    return Arrays.asList("student_name", "course_name", "subscription_date");
  }

  private String nullReplacer(Object o) {
    if (o == null) {
      return "";
    } else {
      return o.toString();
    }
  }
}
