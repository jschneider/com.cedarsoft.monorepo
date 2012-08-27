package com.cedarsoft.annotations.verification;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class VerifyUiThread {
  @Nonnull
  private static UiThreadVerificationStrategy strategy;

  static {
    List<UiThreadVerificationStrategy> delegates = new ArrayList<UiThreadVerificationStrategy>();
    delegates.add(new SwingThreadVerificationStrategy());
    delegates.add(new JavaFxUiThreadVerificationStrategy());
    delegates.add(new SwtUiThreadVerificationStrategy());
    strategy =new DelegatingUiThreadVerificationStrategy(delegates);
  }

  @Nonnull
  public static UiThreadVerificationStrategy getStrategy() {
    return strategy;
  }

  public static void setStrategy(@Nonnull UiThreadVerificationStrategy strategy) {
    VerifyUiThread.strategy = strategy;
  }

  public static void verifyUiThreadAsserted() {
    assert verifyUiThread();
  }

  public static boolean verifyUiThread() {
    if (isUiThread()) {
      return true;
    }

    throw new IllegalStateException( "Called from illegal thread. Must be called from UI thread" );
  }

  private static boolean isUiThread() {
    return strategy.isUiThread();
  }

  public static void verifyNonUiThreadAsserted() {
    assert verifyNonUiThread();
  }

  public static boolean verifyNonUiThread() {
    if (isUiThread()) {
      throw new IllegalStateException( "Called from illegal thread. Must *not* be called from UI thread" );
    }
    return true;
  }
}
