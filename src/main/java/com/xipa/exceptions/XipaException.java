package com.xipa.exceptions;

public class XipaException extends RuntimeException {

  private static final long serialVersionUID = 8287467037334391993L;

  public XipaException() {
    super();
  }

  public XipaException(String message) {
    super(message);
  }

  public XipaException(String message, Throwable cause) {
    super(message, cause);
  }

  public XipaException(Throwable cause) {
    super(cause);
  }

}
