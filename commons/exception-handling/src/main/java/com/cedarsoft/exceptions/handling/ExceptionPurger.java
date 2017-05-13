package com.cedarsoft.exceptions.handling;

import com.cedarsoft.exceptions.CanceledException;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.UncheckedExecutionException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * This is a utility class that is able to work with nested exceptions.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ExceptionPurger {
  /**
   * These exceptions are ignored. No dialog is shown for them.
   */
  @Nonnull
  protected static final ImmutableSet<Class<? extends Throwable>> IGNORED = ImmutableSet.of(CanceledException.class);
  /**
   * These exceptions are purged: They are not reported themselves. Instead the cause is shown in the dialog
   */
  @Nonnull
  protected static final ImmutableSet<String> PURGED = ImmutableSet.of(
    RuntimeException.class.getName()
    , InvocationTargetException.class.getName()
    , ExecutionException.class.getName()
    , UncheckedExecutionException.class.getName()
    , "com.google.inject.ProvisionException"
  );

  private ExceptionPurger() {
  }

  @Nonnull
  public static Throwable purge(@Nonnull Throwable throwable) throws CanceledException {
    //Check for ignoring
    if (IGNORED.contains(throwable.getClass())) {
      throw new CanceledException();
    }

    if (throwable.getCause() != null && PURGED.contains(throwable.getClass().getName())) {
      return purge(throwable.getCause());
    }

    return throwable;
  }

  /**
   * Searches the exception tree and returns the first instance of the given type.
   * This method will return the given Throwable if it is an instance of the given type.
   *
   * @param e             the throwable
   * @param throwableType the type that is looked for
   * @param <T>           the type
   * @return the found exception of the given type or null
   */
  @Nullable
  public static <T extends Throwable> T find(@Nullable Throwable e, @Nonnull Class<T> throwableType) {
    @Nullable
    Throwable current = e;

    while (current != null) {
      if (throwableType.isAssignableFrom(current.getClass())) {
        return throwableType.cast(current);
      }
      current = current.getCause() == current ? null : current.getCause();
    }

    return null;
  }

  @Nonnull
  public static Throwable getRoot(@Nonnull Throwable e) {
    Throwable current = e;

    while (current.getCause() != null) {
      current = current.getCause();
    }

    return current;
  }

  @Nonnull
  public static String getRootMessage(@Nonnull Throwable e) {
    //noinspection ThrowableResultOfMethodCallIgnored
    return getRoot(e).getMessage();
  }

  /**
   * Returns the localized message of the root
   */
  @Nonnull
  public static String getLocalizedRootMessage(@Nonnull Throwable e) {
    //noinspection ThrowableResultOfMethodCallIgnored
    return getRoot(e).getLocalizedMessage();
  }
}
