package com.todotasks.service.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class EntityIsAlreadyExistExceptionTest {

  @Test
  void testCreationAndGetMessage() {
    final String expectedEntityName = "Test";
    final String expectedParamKey = "id";
    final int expectedParamValue = 1;
    final String expectedMessage =
        "Entity='" + expectedEntityName + "' with " + expectedParamKey + "='" + expectedParamValue
            + "' is already exist";
    EntityIsAlreadyExistException actualException = EntityIsAlreadyExistException
        .createWith("id", Integer.toString(expectedParamValue), expectedEntityName);

    assertThat(expectedMessage).isEqualTo(actualException.getMessage());
  }
}