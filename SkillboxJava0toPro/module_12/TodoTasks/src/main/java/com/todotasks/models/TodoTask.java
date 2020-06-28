package com.todotasks.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.*;

@Entity
public class TodoTask {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", columnDefinition = "longtext")
  private String desc;

  @Column(name = "create_date")
  private String createDate;

  @Column(name = "is_done", columnDefinition = "tinyint(1) default 0", nullable = false)
  private Boolean isDone = false;

  public TodoTask() {
    //used by Hibernate
  }

  public TodoTask(String title, String desc) {
    this.title = title;
    this.desc = desc;
    this.createDate = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd\nHH:mm:ss"));
    this.isDone = false;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public Boolean getIsDone() {
    return isDone;
  }

  public void setIsDone(Boolean isDone) {
    this.isDone = isDone;
  }
}
