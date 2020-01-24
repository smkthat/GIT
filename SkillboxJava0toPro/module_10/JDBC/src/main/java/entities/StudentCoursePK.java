package entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
class StudentCoursePK implements Serializable {
  @Column(name = "student_id", insertable=false, updatable=false)
  private Integer studentId;

  @Column(name = "course_id", insertable=false, updatable=false)
  private Integer courseId;

  public StudentCoursePK() {}

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

  // sometimes relations
}
