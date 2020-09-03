package com.todotasks.service;

import com.todotasks.entity.TodoTask;
import com.todotasks.entity.TodoTaskRepo;
import com.todotasks.service.exception.EntityIsAlreadyExistException;
import com.todotasks.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class TodoTaskServiceTest {

  private static final Integer TEST_TODO_TASK_ID = 1;

  @Mock
  private TodoTaskRepo repo;

  @InjectMocks
  private TodoTaskService service;

  private final TodoTask testTodoTask = new TodoTask();
  private final List<TodoTask> testTodoTaskList = new ArrayList<>();

  @BeforeEach
  void prepareTestData() {
    testTodoTask.setId(TEST_TODO_TASK_ID);
    testTodoTask.setTitle("Test");
    testTodoTask.setDesc("For filter");
    testTodoTask.setComplete(false);
    testTodoTask.setToAllDates(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 10));

    testTodoTaskList.addAll(
        Arrays.asList(
            testTodoTask,
            new TodoTask(2, "Test 2", "Test description 2", false),
            new TodoTask(3, "Test 3", "Test description 3", true)));
  }

  @Test
  void getAllTasks() {
    given(repo.findAll()).willReturn(testTodoTaskList);

    List<TodoTask> expected = service.getAllTasks();

    assertThat(testTodoTaskList).usingRecursiveComparison().isEqualTo(expected);

    then(repo).should(times(1)).findAll();
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void getAllTasksShouldReturnEmptyList() {
    final List<TodoTask> emptyList = Collections.emptyList();

    given(repo.findAll()).willReturn(emptyList);

    List<TodoTask> expected = service.getAllTasks();

    assertThat(emptyList).usingRecursiveComparison().isEqualTo(expected);

    then(repo).should(times(1)).findAll();
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void getTask() throws EntityNotFoundException {
    Optional<TodoTask> optionalTodoTask = Optional.of(testTodoTask);

    given(repo.findById(TEST_TODO_TASK_ID)).willReturn(optionalTodoTask);

    TodoTask expected = service.getTask(TEST_TODO_TASK_ID);

    assertThat(testTodoTask).usingRecursiveComparison().isEqualTo(expected);

    then(repo).should(times(1)).findById(TEST_TODO_TASK_ID);
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void getTaskThrowErrorWhenNotFound() {
    given(repo.findById(TEST_TODO_TASK_ID)).willReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.getTask(TEST_TODO_TASK_ID));

    then(repo).should(times(1)).findById(TEST_TODO_TASK_ID);
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void addTask() {
    given(repo.findById(TEST_TODO_TASK_ID)).willReturn(Optional.empty());
    given(repo.save(testTodoTask)).willReturn(testTodoTask);

    Integer id = service.addTask(testTodoTask);

    assertEquals(TEST_TODO_TASK_ID, id);

    then(repo).should(times(1)).findById(TEST_TODO_TASK_ID);
    then(repo).should(times(1)).save(testTodoTask);
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void addTaskThrowErrorWhenAlreadyExist() {
    given(repo.findById(TEST_TODO_TASK_ID)).willReturn(Optional.of(testTodoTask));

    assertThrows(EntityIsAlreadyExistException.class, () -> service.addTask(testTodoTask));

    then(repo).should(times(1)).findById(TEST_TODO_TASK_ID);
    then(repo).should(never()).save(any(TodoTask.class));
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void updateTask() {
    final TodoTask updated = TodoTask.clone(testTodoTask);
    updated.setTitle("Updated");
    updated.setDesc("Updated description");
    updated.setComplete(!testTodoTask.getComplete());

    given(repo.findById(TEST_TODO_TASK_ID)).willReturn(Optional.of(testTodoTask));
    given(repo.save(updated)).willReturn(updated);

    TodoTask expected = service.updateTask(updated, TEST_TODO_TASK_ID);

    assertEquals(expected.getId(), TEST_TODO_TASK_ID);
    assertNotEquals(expected.getTitle(), testTodoTask.getTitle());
    assertNotEquals(expected.getDesc(), testTodoTask.getDesc());
    assertEquals(expected.getCreateDate(), testTodoTask.getCreateDate());
    assertNotEquals(expected.getUpdateDate(), testTodoTask.getUpdateDate());
    assertNotEquals(expected.getComplete(), testTodoTask.getComplete());

    then(repo).should(times(1)).findById(TEST_TODO_TASK_ID);
    then(repo).should(times(1)).save(updated);
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void updateTaskThrowErrorWhenNotFound() {
    given(repo.findById(TEST_TODO_TASK_ID)).willReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class, () -> service.updateTask(testTodoTask, TEST_TODO_TASK_ID));

    then(repo).should(times(1)).findById(TEST_TODO_TASK_ID);
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void deleteTask() {
    given(repo.findById(TEST_TODO_TASK_ID)).willReturn(Optional.of(testTodoTask));

    service.deleteTask(TEST_TODO_TASK_ID);

    then(repo).should(times(1)).findById(TEST_TODO_TASK_ID);
    then(repo).should(times(1)).deleteById(TEST_TODO_TASK_ID);
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void deleteTaskThrowErrorWhenNotFound() {
    given(repo.findById(TEST_TODO_TASK_ID)).willReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.deleteTask(TEST_TODO_TASK_ID));

    then(repo).should(times(1)).findById(TEST_TODO_TASK_ID);
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void deleteAllTasksShouldReturnEmptyList() {
    given(repo.findAll()).willReturn(testTodoTaskList);

    List<TodoTask> expected = service.deleteAllTasks();

    assertThat(Collections.emptyList()).isEqualTo(expected);

    then(repo).should(times(1)).findAll();
    then(repo).should(times(1)).deleteAll();
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void getTaskByEmptyFilterShouldReturnFullTestList() {
    given(repo.findAll()).willReturn(testTodoTaskList);

    List<TodoTask> expected = service.getTasksByFilter("");

    assertThat(testTodoTaskList).usingRecursiveComparison().isEqualTo(expected);
    then(repo).should(times(1)).findAll();
    then(repo).shouldHaveNoMoreInteractions();
  }

  @Test
  void getTaskByFilterShouldReturnListWithOneEntity() {
    List<TodoTask> actual = Collections.singletonList(testTodoTask);
    given(repo.findAll()).willReturn(actual);

    List<TodoTask> expected = service.getTasksByFilter("For filter");

    assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    then(repo).should(times(1)).findAll();
    then(repo).shouldHaveNoMoreInteractions();
  }
}
