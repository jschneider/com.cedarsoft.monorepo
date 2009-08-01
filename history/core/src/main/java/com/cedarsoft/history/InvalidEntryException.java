package com.cedarsoft.history;

public class InvalidEntryException extends RuntimeException {
  public InvalidEntryException() {
  }

  public InvalidEntryException( String message ) {
    super( message );
  }

  public InvalidEntryException( String message, Throwable cause ) {
    super( message, cause );
  }

  public InvalidEntryException( Throwable cause ) {
    super( cause );
  }
}
