package com.todotasks.controllers;

import com.todotasks.models.TodoTask;
import com.todotasks.models.TodoTaskRepo;
import java.net.URI;
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

  @Autowired
  public TodoTaskRepo todoTaskRepo;

  @GetMapping()
  public ResponseEntity<List<TodoTask>> getAll() {
    List<TodoTask> todoTasks = todoTaskRepo.findAll();
    if (!CollectionUtils.isEmpty(todoTasks)) {
      return new ResponseEntity<>(todoTasks, HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<TodoTask> get(@PathVariable("id") int id) {
    Optional<TodoTask> optionalTodoTask = todoTaskRepo.findById(id);
    return optionalTodoTask.map(todoTask -> new ResponseEntity<>(todoTask, HttpStatus.OK))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TodoTask> add(@RequestBody TodoTask todoTask) {
    TodoTask newTodoTask = todoTaskRepo.save(todoTask);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(newTodoTask.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

  @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TodoTask> update(@RequestBody TodoTask todoTask,
      @PathVariable int id) {
    Optional<TodoTask> optionalTodoTask = todoTaskRepo.findById(id);
    if (optionalTodoTask.isPresent()) {
      todoTask.setId(id);
      todoTaskRepo.save(todoTask);
      return new ResponseEntity<>(todoTask, HttpStatus.OK);
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<TodoTask> delete(@PathVariable("id") int id) {
    Optional<TodoTask> optionalTodoTask = todoTaskRepo.findById(id);
    return optionalTodoTask.map(todoTask -> {
      todoTaskRepo.delete(todoTask);
      return new ResponseEntity<>(todoTask, HttpStatus.OK);
    }).orElseGet(() ->
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }
}
