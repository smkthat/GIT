package com.todotasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todotasks.controller.rest.TodoTaskRestController;
import com.todotasks.entity.TodoTask;
import com.todotasks.entity.utils.LocalDateTimeAttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

  public static final String TODO_TASK_ROUT = "/todotasks";
  public static final String TODO_TASK_API_ROUT = "/api/todotasks";
  public static final String SERVER_HOST = "http://localhost";
  public static final String SERVER_PORT = "8080";
  public final LocalDateTimeAttributeConverter dateConverter =
      new LocalDateTimeAttributeConverter();

  public static final int TEST_TODO_TASK_ID = 1;
  public static final int TEST_NOT_EXIST_TODO_TASK_ID = 100;

  public final List<TodoTask> testTodoTaskList = new ArrayList<>();
  public final TodoTask testTodoTask = new TodoTask();

  /**
   * Web application context.
   */
  @Autowired
  protected WebApplicationContext ctx;

  /**
   * Mock mvc.
   */
  @Autowired
  protected MockMvc mockMvc;

  /**
   * Object mapper.
   */
  @Autowired
  protected ObjectMapper mapper;

  /**
   * TodoTask REST api controller.
   */
  @Autowired
  protected TodoTaskRestController todoTaskRestController;

  /**
   * TodoTask controller.
   */
  @Autowired
  protected TodoTaskController todoTaskController;

  /**
   * TodoTask error controller.
   */
  @Autowired
  protected TodoErrorController todoErrorController;

  /**
   * Create mock mvc.
   */
  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
  }
}
