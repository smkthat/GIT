package com.todotasks.service.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EntityNotFoundExceptionTest {

  @Test
  void testCreationAndGetMessage() {
    final String expectedEntityName = "Test";
    final String expectedParamKey = "id";
    final int expectedParamValue = 1;
    final String expectedMessage =
        "Entity='"
            + expectedEntityName
            + "' with "
            + expectedParamKey
            + "='"
            + expectedParamValue
            + "' not found";
    EntityNotFoundException actualException =
        new EntityNotFoundException("id", Integer.toString(expectedParamValue), expectedEntityName);

    assertThat(expectedMessage).isEqualTo(actualException.getMessage());
  }
}
