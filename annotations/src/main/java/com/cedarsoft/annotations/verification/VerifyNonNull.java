package com.cedarsoft.annotations.verification;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.IllegalArgumentException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Deprecated
public class VerifyNonNull {
  /**
   * @noinspection MethodNamesDifferingOnlyByCase
   */
  public static boolean verifyNonNullReturnValue( @Nullable Object returnValue ) {
    if ( returnValue != null ) {
      return true;
    }

    throw new IllegalStateException( "Return value must not be null for method annotated with @Nonnull" );
  }

  public static boolean verifyNonNullParameter( @Nullable Object returnValue, int parameterIndex ) {
    if ( returnValue != null ) {
      return true;
    }

    throw new IllegalArgumentException( "Parameter " + parameterIndex + " must not be null" );
  }
}
