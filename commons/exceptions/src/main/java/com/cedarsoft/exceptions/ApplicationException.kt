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
package com.cedarsoft.exceptions


import java.util.Locale
import javax.annotation.concurrent.Immutable

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 * @noinspection AbstractClassWithoutAbstractMethods
 */
abstract class ApplicationException
/**
 * Creates a new ApplicationException including a cause (which is never presented to the user).
 *
 * @param cause            the cause
 * @param details          the details
 * @param messageArguments the (optional) message arguments
 * @param <T>              the details type
</T> */
protected constructor(
  cause: Throwable?,
  val details: Details,
  vararg messageArguments: Any
) : RuntimeException(cause) {
  /**
   * Creates a new ApplicationException
   *
   * @param details          the details (an enum that implements Details
   * @param messageArguments the (optional) message arguments
   * @param <T>              the details type
  </T> */
  protected constructor(details: Details, vararg messageArguments: Any) : this(null, details, *messageArguments)

  /**
   * Returns the details
   *
   * @return the details
   */
  private val messageArguments: Array<out Any> = messageArguments.clone()

  /**
   * Returns the error code
   *
   * @return the error code
   */
  val errorCode: ErrorCode
    get() = details.errorCode

  /**
   * Returns the title for the default locale
   *
   * @return the title
   */
  val title: String
    get() = details.getTitle(*messageArguments)


  /**
   * Returns the message arguments.
   *
   * @return the message arguments
   */
  fun getMessageArguments(): Array<out Any> {
    return messageArguments.clone()
  }

  /**
   * @return the message (based on the error code)
   */
  override val message: String?
    get() = details.errorCode.toString()

  /**
   * Returns the localized message for the default locale
   *
   * @return the localized message for the default locale
   */
  override fun getLocalizedMessage(): String {
    return details.getLocalizedMessage(*messageArguments)
  }

  /**
   * Returns the localized message for the given locale
   *
   * @param locale the locale
   * @return the localized message for the given locale
   */
  fun getLocalizedMessage(locale: Locale): String {
    return details.getLocalizedMessage(locale, *messageArguments)
  }

  /**
   * Returns the title for the given locale
   *
   * @param locale the locale
   * @return the title for the locale
   */
  fun getTitle(locale: Locale): String {
    return details.getTitle(locale, *messageArguments)
  }

  /**
   * The details for the application exception
   */
  @Immutable
  interface Details {

    /**
     * Returns the error code
     *
     * @return the error code
     */
    val errorCode: ErrorCode

    /**
     * Returns the localized message (without the error code)
     *
     * @param messageArguments the message arguments
     * @return the localized message
     */
    fun getLocalizedMessage(vararg messageArguments: Any): String

    /**
     * Returns the localized message (without the error code)
     *
     * @param locale           the locale
     * @param messageArguments the message arguments
     * @return the localized message
     */
    fun getLocalizedMessage(locale: Locale, vararg messageArguments: Any): String

    /**
     * Returns the title
     *
     * @param messageArguments the message arguments
     * @return the title
     */
    fun getTitle(vararg messageArguments: Any): String

    /**
     * Returns the title
     *
     * @param locale           the locale
     * @param messageArguments the message arguments
     * @return the title
     */
    fun getTitle(locale: Locale, vararg messageArguments: Any): String
  }
}
