package com.xipa.exceptions;

public class ExceptionFactory {

  private ExceptionFactory() {
    // Prevent Instantiation
  }

  public static RuntimeException wrapException(String message, Exception e) {
    return new PersistenceException(/*ErrorContext.instance().message(message).cause(e).toString()*/message, e);
  }

}
