package ru.todolist.entities;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
public class TodoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    private String text;

    public TodoTask() {

    }

    public TodoTask(String title, String text) {
        this.title = title;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
