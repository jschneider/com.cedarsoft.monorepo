package com.cedarsoft.app;

/**
 *
 */
public class UserNotFoundException extends AuthenticationException {
  public UserNotFoundException() {
  }

  public UserNotFoundException( String message ) {
    super( message );
  }

  public UserNotFoundException( String message, Throwable cause ) {
    super( message, cause );
  }

  public UserNotFoundException( Throwable cause ) {
    super( cause );
  }
}
