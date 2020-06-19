package ru.todolist.repos;

import org.springframework.data.repository.CrudRepository;
import ru.todolist.entities.TodoTask;

import java.util.List;

public interface TodoTaskRepo extends CrudRepository<TodoTask,Long> {
    List<TodoTask> findByTitle(String title);
}
