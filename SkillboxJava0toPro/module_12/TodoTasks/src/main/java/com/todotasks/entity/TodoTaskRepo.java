package com.todotasks.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("todo_task")
public interface TodoTaskRepo extends JpaRepository<TodoTask, Integer> {
}
