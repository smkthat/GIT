import javax.persistence.*;

@Entity
@Table(name = "Courses")

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    //@Column(name = "name")
    private String name;

   // @Column(name = "duration")
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private CourseType type;

    //@Column(name = "description")
    private String description;

    @Column(name = "teacher_id")
    private int teacherId;

    @Column(name = "students_count")
    private int studentsCount;

    //@Column(name = "price")
    private int price;

    @Column(name = "price_per_hour")
    private float pricePerHour;

    public Course(int id, String name, int duration, CourseType type, String description, int teacherId, int studentsCount, int price, float pricePerHour) {
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
        //used by Hibernate
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + name + ", duration=" + duration + ", type=" + type + ", description=" + description
                + ", teacher_id=" + teacherId + ", studentsCount=" + studentsCount + ", price=" + price + ", pricePerHour=" + pricePerHour + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(float pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}
