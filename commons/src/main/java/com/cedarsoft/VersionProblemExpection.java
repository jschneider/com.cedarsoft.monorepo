package com.cedarsoft;

/**
 *
 */
public class VersionProblemExpection extends RuntimeException {
  public VersionProblemExpection() {
  }

  public VersionProblemExpection( String message ) {
    super( message );
  }

  public VersionProblemExpection( String message, Throwable cause ) {
    super( message, cause );
  }

  public VersionProblemExpection( Throwable cause ) {
    super( cause );
  }
}
