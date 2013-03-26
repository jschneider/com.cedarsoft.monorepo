package com.cedarsoft.annotations.verification;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class VerifyNonNull {
  /**
   * @noinspection MethodNamesDifferingOnlyByCase
   */
  public static boolean verifyNonNull( @Nullable Object value, @Nonnull String description ) {
    if ( value != null ) {
      return true;
    }

    throw new IllegalStateException( description );
  }
}
