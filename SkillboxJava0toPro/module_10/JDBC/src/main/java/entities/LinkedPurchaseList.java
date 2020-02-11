package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "linked_purchase_list")
public class LinkedPurchaseList {

  @EmbeddedId private Id id;

  @ManyToOne
  @JoinColumn(name = "student_id", updatable = false, insertable = false)
  private Student student;

  @ManyToOne
  @JoinColumn(name = "course_id", updatable = false, insertable = false)
  private Course course;

  @Column(name = "price")
  private Integer price;

  @Column(name = "subscription_date")
  private Date subscriptionDate;

  public Id getId() {
    return id;
  }

  public LinkedPurchaseList() {
    //used by Hibernate
  }

  public LinkedPurchaseList(
      Id id, Student student, Course course, Integer price, Date subscriptionDate) {
    this.id = id;
    this.student = student;
    this.course = course;
    this.price = price;
    this.subscriptionDate = subscriptionDate;
  }

  public void setId(Id id) {
    this.id = id;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Date getSubscriptionDate() {
    return subscriptionDate;
  }

  public void setSubscriptionDate(Date subscriptionDate) {
    this.subscriptionDate = subscriptionDate;
  }

  @Embeddable
  public static class Id implements Serializable {

    @Column(name = "student_id")//, columnDefinition = "INT(11) UNSIGNED")
    private Integer studentId;

    @Column(name = "course_id")//, columnDefinition = "INT(11) UNSIGNED")
    private Integer courseId;

    public Id() {
      // used by Hibernate
    }

    public Id(Integer studentId, Integer courseId) {
      this.studentId = studentId;
      this.courseId = courseId;
    }

    public Integer getStudentId() {
      return studentId;
    }

    public void setStudentId(Integer studentId) {
      this.studentId = studentId;
    }

    public Integer getCourseId() {
      return courseId;
    }

    public void setCourseId(Integer courseId) {
      this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Id id = (Id) o;
      return studentId.equals(id.studentId) && courseId.equals(id.courseId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(studentId, courseId);
    }
  }
}
