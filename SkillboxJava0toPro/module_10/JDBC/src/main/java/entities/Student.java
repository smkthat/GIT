package entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "age")
  private Integer age;

  @Column(name = "registration_date", columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date registrationDate;

  @OneToMany(mappedBy = "student")
  private Set<Subscription> subscriptions;

  public Student(Integer id, String name, Integer age, Date registrationDate) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.registrationDate = registrationDate;
  }

  public Student() {
    // used by Hibernate
  }

  @Override
  public boolean equals(Object other) {
    boolean result = false;
    if (other instanceof Student) {
      Student that = (Student) other;
      result = (this.getId().equals(that.getId()) && this.getName().equals(that.getName()));
    }
    return result;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }

  public Set<Subscription> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(Set<Subscription> subscriptions) {
    this.subscriptions = subscriptions;
  }
}
