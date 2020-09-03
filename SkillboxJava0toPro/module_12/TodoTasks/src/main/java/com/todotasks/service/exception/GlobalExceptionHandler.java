package com.todotasks.service.exception;

import com.todotasks.domain.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Java Code to handel custom exception
 *
 * <p>https://github.com/jovannypcg/exception_handler/
 *
 * @author jovannypcg
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Provides handling for exceptions throughout this service.
   *
   * @param ex The target exception
   * @param request The current request
   */
  @ExceptionHandler({
    EntityNotFoundException.class,
    EntityIsAlreadyExistException.class
  })
  @Nullable
  public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();

    if (ex instanceof EntityNotFoundException) {
      HttpStatus status = HttpStatus.NOT_FOUND;
      EntityNotFoundException notFoundException = (EntityNotFoundException) ex;
      return handleEntityNotFoundException(notFoundException, headers, status, request);
    }

    if (ex instanceof EntityIsAlreadyExistException) {
      HttpStatus status = HttpStatus.BAD_REQUEST;
      EntityIsAlreadyExistException alreadyExistException = (EntityIsAlreadyExistException) ex;
      return handleEntityIsAlreadyExistException(alreadyExistException, headers, status, request);
    }

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  /**
   * Customize the response for EntityNotFoundException.
   *
   * @param ex The exception
   * @param headers The headers to be written to the response
   * @param status The selected response status
   * @return a {@code ResponseEntity} instance
   */
  protected ResponseEntity<ApiError> handleEntityNotFoundException(
      EntityNotFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<String> errors =
        Arrays.asList(status.toString(), LocalDateTime.now().toString(), ex.getMessage());
    return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
  }

  /**
   * Customize the response for EntityIsAlreadyExistException.
   *
   * @param ex The exception
   * @param headers The headers to be written to the response
   * @param status The selected response status
   * @return a {@code ResponseEntity} instance
   */
  protected ResponseEntity<ApiError> handleEntityIsAlreadyExistException(
      EntityIsAlreadyExistException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<String> errors =
        Arrays.asList(status.toString(), LocalDateTime.now().toString(), ex.getMessage());
    return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
  }

  /**
   * A single place to customize the response body of all Exception types.
   *
   * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE} request
   * attribute and creates a {@link ResponseEntity} from the given body, headers, and status.
   *
   * @param ex The exception
   * @param body The body for the response
   * @param headers The headers for the response
   * @param status The response status
   * @param request The current request
   */
  protected ResponseEntity<ApiError> handleExceptionInternal(
      Exception ex,
      @Nullable ApiError body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    if (request != null && HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
    }

    return new ResponseEntity<>(body, headers, status);
  }
}
