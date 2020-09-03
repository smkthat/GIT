package com.todotasks.service;

import com.todotasks.entity.TodoTask;
import com.todotasks.entity.TodoTaskRepo;
import com.todotasks.service.exception.EntityIsAlreadyExistException;
import com.todotasks.service.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class TodoTaskService {

  private static final String SIMPLE_CLASS_NAME = TodoTask.class.getSimpleName();

  private final TodoTaskRepo todoTaskRepo;

  @Autowired
  public TodoTaskService(TodoTaskRepo todoTaskRepo) {
    this.todoTaskRepo = todoTaskRepo;
  }

  public List<TodoTask> getAllTasks() {
    return todoTaskRepo.findAll();
  }

  public TodoTask getTask(int id) {
    return getTaskById(id);
  }

  public Integer addTask(TodoTask todoTask) {
    if (todoTask.getId() != null && todoTaskRepo.findById(todoTask.getId()).isPresent()) {
      throw EntityIsAlreadyExistException.createWith(
          "id", Integer.toString(todoTask.getId()), SIMPLE_CLASS_NAME);
    }

    todoTask.setToAllDates(LocalDateTime.now());
    return todoTaskRepo.save(todoTask).getId();
  }

  public TodoTask updateTask(TodoTask todoTask, int id) {
    TodoTask oldTodoTask = getTaskById(id);

    todoTask.setId(id);
    todoTask.setCreateDate(oldTodoTask.getCreateDate());
    todoTask.setUpdateDate(LocalDateTime.now());
    return todoTaskRepo.save(todoTask);
  }

  public Integer deleteTask(int id) throws EntityNotFoundException {
    getTaskById(id);
    todoTaskRepo.deleteById(id);
    return id;
  }

  public List<TodoTask> deleteAllTasks() {
    List<TodoTask> todoTasks = todoTaskRepo.findAll();
    if (!todoTasks.isEmpty()) {
      todoTaskRepo.deleteAll();
    }
    return Collections.emptyList();
  }

  public List<TodoTask> getTasksByFilter(String filter) {
    return todoTaskRepo.findAll().stream()
            .filter(
                    t ->
                            t.getId().toString().equals(filter)
                                    || t.getTitle().contains(filter)
                                    || t.getDesc().contains(filter))
            .collect(Collectors.toList());
  }

  private TodoTask getTaskById(int id) {
    return todoTaskRepo
        .findById(id)
        .orElseThrow(
            () ->
                EntityNotFoundException.createWith("id", Integer.toString(id), SIMPLE_CLASS_NAME));
  }
}
