//import javax.persistence.*;
//import java.util.Date;
//
//
//@Entity
//@Table(name = "purchaselist")
//public class Purchase {
//
//    private Student student;
//
//    public Student getStudent() {
//        return student;
//    }
//
//
//    private Course course;
//
//    public Course getCourse() {
//        return course;
//    }
//
//
//    @Column(name = "price")
//    private Integer price;
//
//    @Column(name = "subscription_date", columnDefinition = "TIMESTAMP")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date subscriptionDate;
//
//    public Purchase() {
//        //used by Hibernate
//    }
//
//    public Purchase(Student student, Course course, Integer price, Date subscriptionDate) {
//        this.student = student;
//        this.course = course;
//        this.price = price;
//        this.subscriptionDate = subscriptionDate;
//    }
//
//
//    public void setStudent(Student student) {
//        this.student = student;
//    }
//
//    public void setCourse(Course course) {
//        this.course = course;
//    }
//
//    public Integer getPrice() {
//        return price;
//    }
//
//    public void setPrice(Integer price) {
//        this.price = price;
//    }
//
//    public Date getSubscriptionDate() {
//        return subscriptionDate;
//    }
//
//    public void setSubscriptionDate(Date subscriptionDate) {
//        this.subscriptionDate = subscriptionDate;
//    }
//}
