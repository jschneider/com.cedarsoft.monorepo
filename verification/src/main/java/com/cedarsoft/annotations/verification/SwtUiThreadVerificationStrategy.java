package com.cedarsoft.annotations.verification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SwtUiThreadVerificationStrategy implements UiThreadVerificationStrategy {
  @Nullable
  private static Method detectSwtGetCurrentMethod() {
    try {
      Class<?> display = Class.forName("org.eclipse.swt.widgets.Display");
      return display.getMethod("getCurrent");
    } catch (ClassNotFoundException ignore) {
    } catch (NoSuchMethodException ignore) {
    }
    return null;
  }

  @Nullable
  private final Method swtGetCurrentMethod;

  public SwtUiThreadVerificationStrategy() {
    this(detectSwtGetCurrentMethod());
  }

  public SwtUiThreadVerificationStrategy(@Nullable Method swtGetCurrentMethod) {
    this.swtGetCurrentMethod = swtGetCurrentMethod;
  }

  @Override
  public boolean isUiThread() {
    try {
      return swtGetCurrentMethod != null && swtGetCurrentMethod.invoke(null) != null;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e); //TODO remove exception(?)
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e); //TODO remove exception(?)
    }
  }
}
