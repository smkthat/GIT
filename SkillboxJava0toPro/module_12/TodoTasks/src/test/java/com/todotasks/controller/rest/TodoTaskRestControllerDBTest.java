package com.todotasks.controller.rest;

import com.todotasks.controller.AbstractIntegrationTest;
import com.todotasks.entity.TodoTask;
import com.todotasks.service.exception.EntityIsAlreadyExistException;
import com.todotasks.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@Sql(
    value = {"/create-todotasks-before.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(
    value = {"/create-todotasks-after.sql"},
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TodoTaskRestControllerDBTest extends AbstractIntegrationTest {

  private static final String SIMPLE_CLASS_NAME = TodoTask.class.getSimpleName();

  private final List<TodoTask> testTodoTaskList = new ArrayList<>();
  private final TodoTask testTodoTask = new TodoTask();
  private final TodoTask created = new TodoTask("Created", "Creation description", false);
  private final TodoTask updated = new TodoTask("Updated", "Updated description", true);

  @Test
  public void contextLoads() {
    assertThat(todoTaskRestController).isNotNull();
  }

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();

    testTodoTask.setId(TEST_TODO_TASK_ID);
    testTodoTask.setTitle("Test 1");
    testTodoTask.setDesc("Test todo task with id=1");
    testTodoTask.setToAllDates(
        dateConverter.convertToEntityAttribute("2020-01-01 12:00:00.000000000"));
    testTodoTask.setComplete(false);

    testTodoTaskList.addAll(
        Arrays.asList(
            testTodoTask,
            new TodoTask(
                2,
                "Test 2",
                "Test todo task with id=2",
                dateConverter.convertToEntityAttribute("2020-01-01 13:00:00.000000000"),
                dateConverter.convertToEntityAttribute("2020-01-01 13:00:00.000000000"),
                false),
            new TodoTask(
                3,
                "Test 3",
                "Test todo task with id=3",
                dateConverter.convertToEntityAttribute("2020-01-01 14:00:00.000000000"),
                dateConverter.convertToEntityAttribute("2020-01-01 15:00:00.000000000"),
                true)));
  }

  @Test
  void get_shouldReturnTodoTaskByIdAnd200() throws Exception {
    mockMvc
        .perform(get(TODO_TASK_API_ROUT + "/" + TEST_TODO_TASK_ID))
        .andExpect(status().isOk())
        .andExpect(result -> mapper.writeValueAsString(testTodoTask))
        .andExpect(jsonPath("$.id", is(TEST_TODO_TASK_ID)))
        .andExpect(jsonPath("$.title", is(testTodoTask.getTitle())))
        .andExpect(jsonPath("$.desc", is(testTodoTask.getDesc())))
        .andExpect(jsonPath("$.complete", is(testTodoTask.getComplete())))
        .andExpect(
            jsonPath(
                "$.createDate",
                is(testTodoTask.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
        .andExpect(
            jsonPath(
                "$.updateDate",
                is(testTodoTask.getUpdateDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
        .andDo(print());
  }

  @Test
  void get_shouldThrowEntityNotFoundAndReturn404() throws Exception {
    mockMvc
        .perform(get(TODO_TASK_API_ROUT + "/" + TEST_NOT_EXIST_TODO_TASK_ID))
        .andExpect(status().isNotFound())
        .andExpect(
            result ->
                mapper.writeValueAsString(
                    EntityNotFoundException.createWith(
                        "id", Integer.toString(TEST_NOT_EXIST_TODO_TASK_ID), SIMPLE_CLASS_NAME)))
        .andDo(print());
  }

  @Test
  void get_shouldReturnTodoListAnd200() throws Exception {
    mockMvc
        .perform(get(TODO_TASK_API_ROUT))
        .andExpect(status().isOk())
        .andExpect(result -> mapper.writeValueAsString(testTodoTaskList))
        .andExpect(jsonPath("$.size()", is(testTodoTaskList.size())))
        .andDo(print());
  }

  @Test
  void post_shouldReturnTodoTaskAnd201() throws Exception {
    final int expectedId = 10;

    mockMvc
        .perform(
            post(TODO_TASK_API_ROUT)
                .content(mapper.writeValueAsString(created))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(redirectedUrl(SERVER_HOST + TODO_TASK_API_ROUT + "/" + expectedId))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  void post_shouldThrowEntityAlreadyExistAndReturn400() throws Exception {
    created.setId(TEST_TODO_TASK_ID);

    mockMvc
        .perform(
            post(TODO_TASK_API_ROUT)
                .content(mapper.writeValueAsString(created))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                mapper.writeValueAsString(
                    EntityIsAlreadyExistException.createWith(
                        "id", Integer.toString(TEST_TODO_TASK_ID), SIMPLE_CLASS_NAME)))
        .andDo(print());
  }

  @Test
  void patch_shouldReturnTodoTaskAnd200() throws Exception {
    updated.setId(TEST_TODO_TASK_ID);

    mockMvc
        .perform(
            patch(TODO_TASK_API_ROUT + "/" + TEST_TODO_TASK_ID)
                .content(mapper.writeValueAsString(updated))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(TEST_TODO_TASK_ID)))
        .andExpect(jsonPath("$.title", is(updated.getTitle())))
        .andExpect(jsonPath("$.desc", is(updated.getDesc())))
        .andExpect(jsonPath("$.complete", not(testTodoTask.getComplete())))
        .andExpect(
            jsonPath(
                "$.createDate",
                is(testTodoTask.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
        .andExpect(
            jsonPath(
                "$.updateDate",
                not(testTodoTask.getUpdateDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
        .andDo(print());
  }

  @Test
  void patch_shouldThrowEntityNotFoundReturn404() throws Exception {
    updated.setId(TEST_NOT_EXIST_TODO_TASK_ID);

    mockMvc
        .perform(
            patch(TODO_TASK_API_ROUT + "/" + TEST_NOT_EXIST_TODO_TASK_ID)
                .content(mapper.writeValueAsString(updated))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(
            result ->
                mapper.writeValueAsString(
                    EntityNotFoundException.createWith(
                        "id", Integer.toString(TEST_NOT_EXIST_TODO_TASK_ID), SIMPLE_CLASS_NAME)))
        .andDo(print());
  }

  @Test
  void delete_shouldThrowEntityNotFoundReturn404() throws Exception {
    mockMvc
        .perform(delete(TODO_TASK_API_ROUT + "/" + TEST_NOT_EXIST_TODO_TASK_ID))
        .andExpect(status().isNotFound())
        .andExpect(
            result ->
                mapper.writeValueAsString(
                    EntityNotFoundException.createWith(
                        "id", Integer.toString(TEST_NOT_EXIST_TODO_TASK_ID), SIMPLE_CLASS_NAME)))
        .andDo(print());
  }

  @Test
  void delete_shouldDeleteTodoTaskByIdAndReturn200() throws Exception {
    mockMvc
        .perform(delete(TODO_TASK_API_ROUT + "/" + TEST_TODO_TASK_ID))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void delete_shouldDeleteAllTodoTasksReturnEmptyListAnd200() throws Exception {
    mockMvc
        .perform(delete(TODO_TASK_API_ROUT))
        .andExpect(status().isOk())
        .andExpect(result -> mapper.writeValueAsString(Collections.emptyList()))
        .andDo(print());
  }
}
