package com.todotasks.model;

import java.time.LocalDateTime;
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

  @Column(name = "create_date", nullable = false)
  private LocalDateTime createDate;

  @Column(name = "update_date")
  private LocalDateTime updateDate;

  @Column(name = "is_done", columnDefinition = "tinyint(1) default 0", nullable = false)
  private Boolean isDone = false;

  public TodoTask() {
    //used by Hibernate
  }

  public TodoTask(String title, String desc, Boolean isDone) {
    this.title = title;
    this.desc = desc;
    this.createDate = LocalDateTime.now();
    this.updateDate = createDate;
    this.isDone = isDone;
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

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(LocalDateTime updateDate) {
    this.updateDate = updateDate;
  }

  public Boolean getDone() {
    return isDone;
  }

  public void setDone(Boolean done) {
    isDone = done;
  }

  public void setToAllDates(LocalDateTime date) {
    createDate = date;
    updateDate = date;
  }
}
