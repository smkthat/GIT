package com.todotasks.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class TodoTaskRESTControllerTest extends AbstractIntegrationTest {

  @Test
  public void shouldReturnStatusOK() throws Exception {
    super.mockMvc
        .perform(MockMvcRequestBuilders.get("http://localhost:8080/todotask"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void contextLoads() {
    assertThat(todoTaskRESTController).isNotNull();
  }
}
