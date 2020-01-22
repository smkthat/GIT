import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "subscriptions")
public class Subscription {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "subscription_date", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscriptionDate;

    public Subscription() {
        //used by Hibernate
    }

    public Subscription(Student student, Course course, Date subscriptionDate) {
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
}

