package com.cedarsoft.utils.excel;

/**
 *
 */
public class NoValueFoundException extends RuntimeException {
  public NoValueFoundException() {
  }

  public NoValueFoundException( String message ) {
    super( message );
  }

  public NoValueFoundException( String message, Throwable cause ) {
    super( message, cause );
  }

  public NoValueFoundException( Throwable cause ) {
    super( cause );
  }
}
