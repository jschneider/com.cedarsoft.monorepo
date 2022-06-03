/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package com.cedarsoft.exceptions.handling

import com.cedarsoft.exceptions.CanceledException
import com.google.common.collect.ImmutableSet
import com.google.common.util.concurrent.UncheckedExecutionException
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ExecutionException

/**
 * This is a utility class that is able to work with nested exceptions.
 *
 */
object ExceptionPurger {
  /**
   * These exceptions are ignored. No dialog is shown for them.
   */
  private val ignored: ImmutableSet<Class<out Throwable>> = ImmutableSet.of(CanceledException::class.java)

  /**
   * These exceptions are purged: They are not reported themselves. Instead the cause is shown in the dialog
   */
  private val purged: ImmutableSet<String> = ImmutableSet.of(
    RuntimeException::class.java.name, InvocationTargetException::class.java.name, ExecutionException::class.java.name, UncheckedExecutionException::class.java.name, "com.google.inject.ProvisionException"
  )

  @JvmStatic
  @Throws(CanceledException::class)
  fun purge(throwable: Throwable): Throwable {
    //Check for ignoring
    if (ignored.contains(throwable.javaClass)) {
      throw CanceledException()
    }

    val cause = throwable.cause

    return if (cause != null && purged.contains(throwable.javaClass.name)) {
      purge(cause)
    } else throwable
  }

  /**
   * Searches the exception tree and returns the first instance of the given type.
   * This method will return the given Throwable if it is an instance of the given type.
   *
   * @param e             the throwable
   * @param throwableType the type that is looked for
   * @param <T>           the type
   * @return the found exception of the given type or null
  </T> */
  fun <T : Throwable> find(e: Throwable?, throwableType: Class<T>): T? {
    var current = e

    while (current != null) {
      if (throwableType.isAssignableFrom(current.javaClass)) {
        return throwableType.cast(current)
      }
      current = if (current.cause === current) null else current.cause
    }

    return null
  }

  fun getRoot(e: Throwable): Throwable {
    var current = e

    val cause = current.cause
    while (cause != null) {
      current = cause
    }

    return current
  }

  fun getRootMessage(e: Throwable): String? {
    return getRoot(e).message
  }

  /**
   * Returns the localized message of the root
   */
  fun getLocalizedRootMessage(e: Throwable): String {

    return getRoot(e).localizedMessage
  }
}
