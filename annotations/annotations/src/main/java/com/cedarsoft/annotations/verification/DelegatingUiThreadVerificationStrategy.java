package com.cedarsoft.annotations.verification;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Deprecated
public class DelegatingUiThreadVerificationStrategy implements UiThreadVerificationStrategy {
  @Nonnull
  private final List<? extends UiThreadVerificationStrategy> delegates;

  public DelegatingUiThreadVerificationStrategy(@Nonnull List<? extends UiThreadVerificationStrategy> delegates) {
    this.delegates = new ArrayList<UiThreadVerificationStrategy>(delegates);
  }

  @Override
  public boolean isUiThread() {
    for (UiThreadVerificationStrategy delegate : delegates) {
      if (delegate.isUiThread()) {
        return true;
      }
    }
    return false;
  }
}
