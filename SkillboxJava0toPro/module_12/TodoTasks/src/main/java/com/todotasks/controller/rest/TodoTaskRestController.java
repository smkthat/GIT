package com.todotasks.controller.rest;

import com.todotasks.entity.TodoTask;
import com.todotasks.service.TodoTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/todotasks")
public class TodoTaskRestController {

  private final TodoTaskService todoTaskService;

  public TodoTaskRestController(TodoTaskService todoTaskService) {
    this.todoTaskService = todoTaskService;
  }

  @GetMapping
  public ResponseEntity<List<TodoTask>> getAll() {
    return ResponseEntity.ok(todoTaskService.getAllTasks());
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<TodoTask> get(@PathVariable("id") int id) {
    return ResponseEntity.ok(todoTaskService.getTask(id));
  }

  @PostMapping
  public ResponseEntity<TodoTask> add(@RequestBody TodoTask todoTask) {
    int id = todoTaskService.addTask(todoTask);
    return ResponseEntity.created(
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri())
        .build();
  }

  @PatchMapping(value = "/{id}")
  public ResponseEntity<TodoTask> update(@RequestBody TodoTask todoTask, @PathVariable int id) {
    return ResponseEntity.ok(todoTaskService.updateTask(todoTask, id));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Integer> delete(@PathVariable("id") int id) {
    todoTaskService.deleteTask(id);
    return ResponseEntity.ok(id);
  }

  @DeleteMapping
  public ResponseEntity<List<TodoTask>> deleteAll() {
    return ResponseEntity.ok(todoTaskService.deleteAllTasks());
  }
}
