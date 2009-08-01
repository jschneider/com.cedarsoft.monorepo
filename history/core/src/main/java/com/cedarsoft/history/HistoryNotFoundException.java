package com.cedarsoft.history;

/**
 *
 */
public class HistoryNotFoundException extends RuntimeException {
  public HistoryNotFoundException() {
  }

  public HistoryNotFoundException( String message ) {
    super( message );
  }

  public HistoryNotFoundException( String message, Throwable cause ) {
    super( message, cause );
  }

  public HistoryNotFoundException( Throwable cause ) {
    super( cause );
  }
}
