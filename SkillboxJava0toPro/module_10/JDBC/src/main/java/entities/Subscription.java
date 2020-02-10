package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "subscriptions")
public class Subscription {
  @EmbeddedId private Id id;

  @ManyToOne
  @JoinColumn(name = "student_id", insertable = false, updatable = false)
  private Student student;

  @ManyToOne
  @JoinColumn(name = "course_id", insertable = false, updatable = false)
  private Course course;

  @Column(name = "subscription_date", columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date subscriptionDate;

  public Subscription() {
    // used by Hibernate
  }

  public Subscription(Id id, Student student, Course course, Date subscriptionDate) {
    this.id = id;
    this.student = student;
    this.course = course;
    this.subscriptionDate = subscriptionDate;
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

  public Date getSubscriptionDate() {
    return subscriptionDate;
  }

  public void setSubscriptionDate(Date subscriptionDate) {
    this.subscriptionDate = subscriptionDate;
  }

  public Id getId() {
    return id;
  }

  public void setId(Id id) {
    this.id = id;
  }

  @Embeddable
  private static class Id implements Serializable {

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "course_id")
    private String courseId;

    public String getStudentId() {
      return studentId;
    }

    public void setStudentId(String studentId) {
      this.studentId = studentId;
    }

    public String getCourseId() {
      return courseId;
    }

    public void setCourseId(String courseId) {
      this.courseId = courseId;
    }

    public Id() {
      // used by Hibernate
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
