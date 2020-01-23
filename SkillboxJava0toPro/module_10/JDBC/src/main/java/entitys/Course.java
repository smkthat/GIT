package entitys;

import entitys.enums.CourseType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "duration")
  private Integer duration;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "enum")
  private CourseType type;

  @Column(name = "description")
  private String description;

  @Column(name = "teacher_id")
  private Integer teacherId;

  @Column(name = "students_count")
  private Integer studentsCount;

  @Column(name = "price")
  private Integer price;

  @Column(name = "price_per_hour")
  private Float pricePerHour;

  @OneToMany
  @JoinColumn(name = "course_id")
  private Set<Subscription> subscriptions;

  public Course(
      Integer id,
      String name,
      Integer duration,
      CourseType type,
      String description,
      Integer teacherId,
      Integer studentsCount,
      Integer price,
      Float pricePerHour) {
    this.id = id;
    this.name = name;
    this.duration = duration;
    this.type = type;
    this.description = description;
    this.teacherId = teacherId;
    this.studentsCount = studentsCount;
    this.price = price;
    this.pricePerHour = pricePerHour;
  }

  public Course() {
    // used by Hibernate
  }

  @Override
  public String toString() {
    return "entitys.Course [id="
        + id
        + ", name="
        + name
        + ", duration="
        + duration
        + ", type="
        + type
        + ", description="
        + description
        + ", teacher_id="
        + teacherId
        + ", studentsCount="
        + studentsCount
        + ", price="
        + price
        + ", pricePerHour="
        + pricePerHour
        + "]";
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

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public CourseType getType() {
    return type;
  }

  public void setType(CourseType type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(Integer teacherId) {
    this.teacherId = teacherId;
  }

  public Integer getStudentsCount() {
    return studentsCount;
  }

  public void setStudentsCount(Integer studentsCount) {
    this.studentsCount = studentsCount;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Float getPricePerHour() {
    return pricePerHour;
  }

  public void setPricePerHour(Float pricePerHour) {
    this.pricePerHour = pricePerHour;
  }

  public Set<Subscription> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(Set<Subscription> subscriptions) {
    this.subscriptions = subscriptions;
  }
}
