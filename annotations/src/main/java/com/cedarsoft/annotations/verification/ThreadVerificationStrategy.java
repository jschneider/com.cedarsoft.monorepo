package com.cedarsoft.annotations.verification;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface ThreadVerificationStrategy {
  void verifyThread(@Nonnull String... threadDescriptors);
}
