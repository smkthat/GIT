package com.todotasks.controller;

import com.todotasks.entity.TodoTask;
import com.todotasks.service.TodoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoTaskController {
  private final TodoTaskService todoTaskService;

  @Autowired
  public TodoTaskController(TodoTaskService todoTaskService) {
    this.todoTaskService = todoTaskService;
  }

  @GetMapping("/todotasks")
  public String redirect() {
    return "redirect:/";
  }

  @GetMapping("/")
  public String getAll(Model model) {
    model.addAttribute("todoTasks", todoTaskService.getAllTasks());
    model.addAttribute("newTask", new TodoTask());
    return "home";
  }

  @PostMapping("/filter")
  public String filter(@RequestParam String filter, Model model) {
    model.addAttribute("todoTasks", todoTaskService.getTasksByFilter(filter));
    model.addAttribute("newTask", new TodoTask());
    return "home";
  }

  @GetMapping(value = "/todotasks/{id}")
  public String get(@PathVariable("id") int id, Model model) {
    model.addAttribute("todoTask", todoTaskService.getTask(id));
    return "task";
  }

  @PostMapping("/todotasks")
  public String add(@ModelAttribute TodoTask newTask, BindingResult result) {
    if (result.hasErrors()) {
      return "redirect:/error";
    }
    todoTaskService.addTask(newTask);
    return "redirect:/";
  }

  @PostMapping(value = "/todotasks/update={id}")
  public String update(
      @ModelAttribute TodoTask todoTask, @PathVariable int id, BindingResult result) {
    if (result.hasErrors()) {
      return "redirect:/error";
    }
    todoTaskService.updateTask(todoTask, id);
    return "redirect:/";
  }

  @GetMapping(value = "/todotasks/delete={id}")
  public String delete(@PathVariable int id) {
    todoTaskService.deleteTask(id);
    return "redirect:/";
  }

  @PostMapping("/todotasks/deleteAll")
  public String deleteAll() {
    todoTaskService.deleteAllTasks();
    return "redirect:/";
  }
}
