package com.todotasks.controller;

import com.todotasks.model.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/todotask")
public class TodoTaskRESTController {

  @Autowired public TodoTaskRepo todoTaskRepo;

  @GetMapping()
  public ResponseEntity<List<TodoTask>> getAll() {
    List<TodoTask> todoTasks = todoTaskRepo.findAll();
    if (!CollectionUtils.isEmpty(todoTasks)) {
      return ResponseEntity.ok(todoTasks);
    }
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TodoTask> get(@PathVariable("id") int id) {
    Optional<TodoTask> optionalTodoTask = todoTaskRepo.findById(id);
    return optionalTodoTask
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TodoTask> add(@RequestBody TodoTask todoTask) {
    todoTask.setToAllDates(LocalDateTime.now());
    int id = todoTaskRepo.save(todoTask).getId();
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    return ResponseEntity.created(location).build();
  }

  @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TodoTask> update(@RequestBody TodoTask todoTask, @PathVariable int id) {
    Optional<TodoTask> optionalTodoTask = todoTaskRepo.findById(id);
    if (optionalTodoTask.isPresent()) {
      todoTask.setId(id);
      todoTask.setCreateDate(optionalTodoTask.get().getCreateDate());
      todoTask.setUpdateDate(LocalDateTime.now());
      todoTaskRepo.save(todoTask);
      return ResponseEntity.ok(todoTask);
    }
    return ResponseEntity.notFound().build();
  }

  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TodoTask> save(@RequestBody TodoTask todoTask, @PathVariable("id") int id) {
    todoTask.setId(id);
    todoTaskRepo.save(todoTask);
    return ResponseEntity.ok(todoTask);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<TodoTask> delete(@PathVariable("id") int id) {
    Optional<TodoTask> optionalTodoTask = todoTaskRepo.findById(id);
    return optionalTodoTask
        .map(
            todoTask -> {
              todoTaskRepo.delete(todoTask);
              return ResponseEntity.ok(todoTask);
            })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
