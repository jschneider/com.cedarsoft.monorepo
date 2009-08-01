package com.cedarsoft.async;

/**
 *
 */
public class AsynchroniousInvocationException extends RuntimeException {
  public AsynchroniousInvocationException() {
  }

  public AsynchroniousInvocationException( String message ) {
    super( message );
  }

  public AsynchroniousInvocationException( String message, Throwable cause ) {
    super( message, cause );
  }

  public AsynchroniousInvocationException( Throwable cause ) {
    super( cause );
  }
}
