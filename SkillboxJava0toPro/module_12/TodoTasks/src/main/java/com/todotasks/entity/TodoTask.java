package com.todotasks.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TodoTask {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", columnDefinition = "longtext")
  private String desc;

  @Column(name = "create_date", nullable = false)
  private LocalDateTime createDate;

  @Column(name = "update_date")
  private LocalDateTime updateDate;

  @Column(name = "complete", columnDefinition = "tinyint(1) default 0", nullable = false)
  private Boolean complete = false;

  public TodoTask() {
    // used by Hibernate
  }

  public TodoTask(final int id, final String title, final String desc, final Boolean complete) {
    this.id = id;
    this.title = title;
    this.desc = desc;
    this.createDate = LocalDateTime.now();
    this.updateDate = createDate;
    this.complete = complete;
  }

  public TodoTask(
      final Integer id,
      final String title,
      final String desc,
      final LocalDateTime createDate,
      final LocalDateTime updateDate,
      final Boolean complete) {
    this.id = id;
    this.title = title;
    this.desc = desc;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.complete = complete;
  }

  public static TodoTask clone(final TodoTask target) {
    return new TodoTask(
        target.getId(),
        target.getTitle(),
        target.getDesc(),
        target.getCreateDate(),
        target.getUpdateDate(),
        target.getComplete());
  }

  public TodoTask(final String title, final String desc, final Boolean complete) {
    this.title = title;
    this.desc = desc;
    this.createDate = LocalDateTime.now();
    this.updateDate = createDate;
    this.complete = complete;
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

  public Boolean getComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }

  public void setToAllDates(LocalDateTime dateTime) {
    createDate = dateTime;
    updateDate = dateTime;
  }
}
