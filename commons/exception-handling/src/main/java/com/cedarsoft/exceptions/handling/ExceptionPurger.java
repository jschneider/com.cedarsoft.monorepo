/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
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
