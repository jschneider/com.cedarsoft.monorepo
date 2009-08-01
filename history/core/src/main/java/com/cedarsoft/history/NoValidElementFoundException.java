package com.cedarsoft.history;

/**
 *
 */
public class NoValidElementFoundException extends NoElementFoundException {
  public NoValidElementFoundException() {
  }

  public NoValidElementFoundException( String message ) {
    super( message );
  }

  public NoValidElementFoundException( String message, Throwable cause ) {
    super( message, cause );
  }

  public NoValidElementFoundException( Throwable cause ) {
    super( cause );
  }
}
