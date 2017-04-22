package com.cedarsoft.maven.instrumentation.plugin;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ClassTransformationException extends Exception {
  public ClassTransformationException() {
    super();
  }

  public ClassTransformationException( final String message,
                                       final Throwable cause ) {
    super( message, cause );
  }

  public ClassTransformationException( final String message ) {
    super( message );
  }

  public ClassTransformationException( final Throwable cause ) {
    super( cause );
  }

}
