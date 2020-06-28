package ru.todolist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.todolist.repos.TodoTaskRepo;
import ru.todolist.entities.TodoTask;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
public class MainController {

  @Autowired private TodoTaskRepo taskRepo;

  @GetMapping("/")
  public String root() {
    return "redirect:/home";
  }

  @GetMapping("/home")
  public String home(Model model) {
    model.addAttribute("todoTasks", taskRepo.findAll());
    return "home";
  }

  @PostMapping("/home")
  public String add(@RequestParam String title, @RequestParam String text, Model model) {
    taskRepo.save(new TodoTask(title, text));
    model.addAttribute("todoTasks", taskRepo.findAll());
    return "home";
  }

  @PostMapping("filter")
  public String filter(@RequestParam String filter, Model model) {
    Iterable<TodoTask> filtered = taskRepo.findAll();
    if (filter != null && !filter.isEmpty()) {
      ArrayList<TodoTask> taskList = new ArrayList<>();
      filtered.forEach(taskList::add);
      filtered =
          taskList.stream()
              .filter(
                  todo ->
                      todo.getId().toString().contains(filter)
                          || todo.getTitle().contains(filter)
                          || todo.getText().contains(filter))
              .collect(Collectors.toList());
    }

    model.addAttribute("todoTasks", filtered);
    return "home";
  }
}
