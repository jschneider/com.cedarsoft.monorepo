package com.cedarsoft.history;

/**
 *
 */
public class NoElementFoundException extends RuntimeException {
  public NoElementFoundException() {
  }

  public NoElementFoundException( String message ) {
    super( message );
  }

  public NoElementFoundException( String message, Throwable cause ) {
    super( message, cause );
  }

  public NoElementFoundException( Throwable cause ) {
    super( cause );
  }
}
