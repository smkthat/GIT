package com.todotasks.models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoTaskRepo extends JpaRepository<TodoTask, Integer> {
  //
}
