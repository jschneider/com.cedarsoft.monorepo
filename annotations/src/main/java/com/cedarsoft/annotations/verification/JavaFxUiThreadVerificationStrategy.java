package com.cedarsoft.annotations.verification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JavaFxUiThreadVerificationStrategy implements UiThreadVerificationStrategy {
  @Nullable
  private static Method detectFxMethod() {
    try {
      Class<?> display = Class.forName("javafx.application.Platform");
      return display.getMethod("isFxApplicationThread");
    } catch (ClassNotFoundException ignore) {
    } catch (NoSuchMethodException ignore) {
    }
    return null;
  }

  @Nullable
  private final Method fxIsFxApplicationThreadMethod;

  public JavaFxUiThreadVerificationStrategy() {
    this(detectFxMethod());
  }

  public JavaFxUiThreadVerificationStrategy(@Nullable Method fxIsFxApplicationThreadMethod) {
    this.fxIsFxApplicationThreadMethod = fxIsFxApplicationThreadMethod;
  }

  @Override
  public boolean isUiThread() {
    try {
      return fxIsFxApplicationThreadMethod != null && fxIsFxApplicationThreadMethod.invoke(null) == Boolean.TRUE;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e); //TODO remove exception(?)
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e); //TODO remove exception(?)
    }
  }
}
