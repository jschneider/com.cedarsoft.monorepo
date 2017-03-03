package com.cedarsoft.annotations.verification;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class VerifyThread {
  @Nonnull
  private static ThreadVerificationStrategy strategy;

  static {
    List<ThreadVerificationStrategy> delegates = new ArrayList<ThreadVerificationStrategy>();
    //delegates.add(new SwingThreadVerificationStrategy());
    //delegates.add(new JavaFxUiThreadVerificationStrategy());
    //delegates.add(new SwtUiThreadVerificationStrategy());
    strategy =new DelegatingThreadVerificationStrategy(delegates);
  }

  private VerifyThread() {
  }

  @Nonnull
  public static ThreadVerificationStrategy getStrategy() {
    return strategy;
  }

  public static void setStrategy(@Nonnull ThreadVerificationStrategy strategy) {
    VerifyThread.strategy = strategy;
  }

  public static void verifyThread(@Nonnull String... threadDescriptors) {
    getStrategy().verifyThread(threadDescriptors);
  }
}
