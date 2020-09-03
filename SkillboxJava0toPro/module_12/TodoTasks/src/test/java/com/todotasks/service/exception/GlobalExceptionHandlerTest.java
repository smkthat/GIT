package com.todotasks.service.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.todotasks.domain.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

  @Test
  void handleException() {
    EntityNotFoundException notFoundException = EntityNotFoundException
        .createWith("id", "1", "NotFound");
    EntityIsAlreadyExistException existException = EntityIsAlreadyExistException
        .createWith("id", "2", "AlreadyExist");
    RuntimeException ex = new RuntimeException();

    GlobalExceptionHandler handler = new GlobalExceptionHandler();
    ResponseEntity<ApiError> notFoundResponse = handler.handleException(notFoundException, null);
    ResponseEntity<ApiError> existResponse = handler.handleException(existException, null);
    ResponseEntity<ApiError> exResponse = handler.handleException(ex, null);

    assert notFoundResponse != null;
    assertThat(notFoundResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assert existResponse != null;
    assertThat(existResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assert exResponse != null;
    assertThat(exResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}