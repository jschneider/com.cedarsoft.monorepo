package com.cedarsoft.utils;

/**
 *
 */
public class AuthentificationException extends Exception {
  public AuthentificationException() {
  }

  public AuthentificationException( String message ) {
    super( message );
  }

  public AuthentificationException( String message, Throwable cause ) {
    super( message, cause );
  }

  public AuthentificationException( Throwable cause ) {
    super( cause );
  }
}
