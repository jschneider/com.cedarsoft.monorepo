package com.cedarsoft.annotations.verification;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DelegatingThreadVerificationStrategy implements ThreadVerificationStrategy {
  @Nonnull
  private final List<? extends ThreadVerificationStrategy> delegates;

  public DelegatingThreadVerificationStrategy(@Nonnull List<? extends ThreadVerificationStrategy> delegates) {
    this.delegates = new ArrayList<ThreadVerificationStrategy>(delegates);
  }

  @Override
  public void verifyThread(@Nonnull String... threadDescriptors) {
    for (ThreadVerificationStrategy delegate : delegates) {
      delegate.verifyThread(threadDescriptors);
    }
  }
}
