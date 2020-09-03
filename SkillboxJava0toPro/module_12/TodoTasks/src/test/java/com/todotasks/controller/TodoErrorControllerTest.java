package com.todotasks.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TodoErrorControllerTest extends AbstractIntegrationTest {

  @Test
  public void contextLoads() {
    assertThat(todoErrorController).isNotNull();
  }

  @Test
  public void get_shouldReturnStatusNotFound() throws Exception {
    mockMvc
            .perform(get("http://localhost:8080/fff"))
            .andDo(print())
            .andExpect(status().isNotFound());
  }
}