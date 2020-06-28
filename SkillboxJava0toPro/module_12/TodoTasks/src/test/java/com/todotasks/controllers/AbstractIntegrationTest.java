package com.todotasks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {
  /** Web application context. */
  @Autowired protected WebApplicationContext ctx;

  /** Mock mvc. */
  @Autowired protected MockMvc mockMvc;

  /** Object mapper. */
  @Autowired protected ObjectMapper mapper;

  /** TodoTask controller. */
  @Autowired protected TodoTaskRESTController todoTaskRESTController;

  /** Main controller. */
  @Autowired protected MainRESTController mainRESTController;

  /** Create mock mvc. */
  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
  }
}
