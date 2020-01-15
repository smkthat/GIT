import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Hiber implements AutoCloseable {

  private SessionFactory sessionFactory;
  @Override
  public void close() {
    sessionFactory.close();
  }

  Hiber() {
    Configuration cfg =
        new Configuration()
            .addResource("hibernate.cfg.xml")
            .addResource("courses.hbm.xml")
            .configure();

    ServiceRegistry registry =
        new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();

//     Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
//     sessionFactory = metadata.getSessionFactoryBuilder().build();
    sessionFactory = cfg.buildSessionFactory(registry);
  }

  public void executeHQuery(String query) {
    try (Session session = sessionFactory.openSession()) {
      List<Course> courses = session.createQuery(query, Course.class).list();
      System.out.println(Arrays.toString(courses.toArray()));
    } catch (QuerySyntaxException qse) {
      qse.printStackTrace();
    }
  }
}
