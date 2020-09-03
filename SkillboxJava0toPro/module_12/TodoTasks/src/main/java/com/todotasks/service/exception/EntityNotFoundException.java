package com.todotasks.service.exception;

public class EntityNotFoundException extends RuntimeException {

  private String entityName;
  private String paramKey;
  private String paramValue;

  public static EntityNotFoundException createWith(String paramKey, String paramValue,
      String entityName) {
    return new EntityNotFoundException(paramKey, paramValue, entityName);
  }

  private EntityNotFoundException(String paramKey, String paramValue, String entityName) {
    this.paramKey = paramKey;
    this.paramValue = paramValue;
    this.entityName = entityName;
  }

  @Override
  public String getMessage() {
    return "Entity='" + entityName + "' with " + paramKey + "='" + paramValue + "' not found";
  }
}
