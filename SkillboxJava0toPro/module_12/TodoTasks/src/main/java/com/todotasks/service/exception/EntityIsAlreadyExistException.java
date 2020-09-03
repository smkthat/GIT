package com.todotasks.service.exception;

public class EntityIsAlreadyExistException extends RuntimeException {

  private String entityName;
  private String paramKey;
  private String paramValue;

  public static EntityIsAlreadyExistException createWith(String paramKey, String paramValue,
      String entityName) {
    return new EntityIsAlreadyExistException(paramKey, paramValue, entityName);
  }

  private EntityIsAlreadyExistException(String paramKey, String paramValue, String entityName) {
    this.paramKey = paramKey;
    this.paramValue = paramValue;
    this.entityName = entityName;
  }

  @Override
  public String getMessage() {
    return "Entity='" + entityName + "' with " + paramKey + "='" + paramValue
        + "' is already exist";
  }
}
