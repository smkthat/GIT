package com.todotasks.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoTaskRepo extends JpaRepository<TodoTask, Integer> {
}
