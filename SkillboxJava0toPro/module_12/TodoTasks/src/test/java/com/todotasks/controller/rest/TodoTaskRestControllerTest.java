package com.todotasks.controller.rest;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.todotasks.controller.AbstractIntegrationTest;
import com.todotasks.entity.TodoTask;
import com.todotasks.service.TodoTaskService;
import com.todotasks.service.exception.EntityIsAlreadyExistException;
import com.todotasks.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TodoTaskRestControllerTest extends AbstractIntegrationTest {

  @MockBean private TodoTaskService service;

  @Autowired
  protected TodoTaskRestController todoTaskRestController;

  private final List<TodoTask> testTodoTaskList = new ArrayList<>();
  private final TodoTask testTodoTask = new TodoTask();

  @Test
  public void contextLoads() {
    assertThat(todoTaskRestController).isNotNull();
  }

  @BeforeEach
  public void setup() {
    testTodoTask.setId(TEST_TODO_TASK_ID);
    testTodoTask.setTitle("Test");
    testTodoTask.setDesc("Test description");
    testTodoTask.setToAllDates(LocalDateTime.parse("2020-07-06T11:53:05"));
    testTodoTask.setComplete(false);

    testTodoTaskList.addAll(
        Arrays.asList(
            testTodoTask,
            new TodoTask(
                2,
                "Test 2",
                "Test todo task with id=2",
                LocalDateTime.parse("2020-05-06T11:53:05"),
                LocalDateTime.parse("2020-07-06T11:53:05"),
                false),
            new TodoTask(
                3,
                "Test 3",
                "Test todo task with id=3",
                LocalDateTime.parse("2020-07-06T11:53:05"),
                LocalDateTime.parse("2020-07-07T11:53:05"),
                true)));
  }

  @Test
  public void get_shouldReturn200() throws Exception {
    mockMvc.perform(get(TODO_TASK_API_ROUT)).andExpect(status().isOk()).andDo(print());
  }

  @Test
  void get_shouldReturnAllTodoTasksAnd200() throws Exception {
    given(service.getAllTasks()).willReturn(testTodoTaskList);
    mockMvc
        .perform(get(TODO_TASK_API_ROUT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(testTodoTaskList.size())))
        .andDo(print());
  }

  @Test
  void get_shouldReturnEmptyListAnd200() throws Exception {
    given(service.getAllTasks()).willReturn(Collections.emptyList());
    mockMvc
        .perform(get(TODO_TASK_API_ROUT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(0)))
        .andDo(print());
  }

  @Test
  void get_shouldReturnTodoTaskByIdAnd200() throws Exception {
    given(service.getTask(TEST_TODO_TASK_ID)).willReturn(testTodoTask);

    mockMvc
        .perform(get(TODO_TASK_API_ROUT + "/{id}", TEST_TODO_TASK_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(TEST_TODO_TASK_ID)))
        .andExpect(jsonPath("$.title", is(testTodoTask.getTitle())))
        .andExpect(jsonPath("$.desc", is(testTodoTask.getDesc())))
        .andExpect(jsonPath("$.complete", is(testTodoTask.getComplete())))
        .andDo(print());
  }

  @Test
  void get_shouldThrowEntityNotFoundWhenGettingTaskByIdAndReturn404() throws Exception {
    given(service.getTask(TEST_TODO_TASK_ID)).willThrow(EntityNotFoundException.class);

    mockMvc
        .perform(get(TODO_TASK_API_ROUT + "/{id}", TEST_TODO_TASK_ID))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  void post_shouldAddTaskAndReturn201() throws Exception {
    given(service.addTask(any(TodoTask.class))).willReturn(anyInt());

    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

    mockMvc
        .perform(
            post(TODO_TASK_API_ROUT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testTodoTask)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  void post_shouldThrowEntityAlreadyExistErrorWhenPostingExistTaskAndReturn400() throws Exception {
    given(service.addTask(any(TodoTask.class))).willThrow(EntityIsAlreadyExistException.class);

    mockMvc.perform(post(TODO_TASK_API_ROUT)).andExpect(status().isBadRequest()).andDo(print());
  }

  @Test
  void patch_shouldThrowEntityNotFoundErrorWhenPatchingNotExistTaskAndReturn404() throws Exception {
    given(service.updateTask(any(TodoTask.class), anyInt()))
        .willThrow(EntityNotFoundException.class);

    mockMvc
        .perform(
            patch(TODO_TASK_API_ROUT + "/{id}", 777)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testTodoTask)))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  void patch_shouldUpdateTodoTaskAndReturn200() throws Exception {
    TodoTask toUpdate = TodoTask.clone(testTodoTask);
    toUpdate.setTitle("Updated");

    given(service.updateTask(any(TodoTask.class), anyInt())).willReturn(any(TodoTask.class));

    mockMvc
        .perform(
            patch(TODO_TASK_API_ROUT + "/{id}", anyInt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(toUpdate)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void delete_shouldDeleteTaskAndReturn200() throws Exception {
    given(service.deleteTask(anyInt())).willReturn(anyInt());

    mockMvc
        .perform(delete(TODO_TASK_API_ROUT + "/{id}", TEST_TODO_TASK_ID))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void delete_shouldThrowEntityNotFoundErrorWhenDeletingNonExistTaskAndReturn404()
      throws Exception {
    given(service.deleteTask(anyInt())).willThrow(EntityNotFoundException.class);

    mockMvc
        .perform(delete(TODO_TASK_API_ROUT + "/{id}", TEST_TODO_TASK_ID))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  void delete_shouldDeleteAllTasksAndReturn200() throws Exception {
    given(service.deleteAllTasks()).willReturn(Collections.emptyList());

    mockMvc.perform(delete(TODO_TASK_API_ROUT)).andExpect(status().isOk()).andDo(print());
  }
}
