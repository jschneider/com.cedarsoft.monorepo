package com.cedarsoft;

/**
 * Common exception for all kinds of version related problems
 */
public class VersionException extends RuntimeException {
  public VersionException() {
  }

  public VersionException( String message ) {
    super( message );
  }

  public VersionException( String message, Throwable cause ) {
    super( message, cause );
  }

  public VersionException( Throwable cause ) {
    super( cause );
  }
}
