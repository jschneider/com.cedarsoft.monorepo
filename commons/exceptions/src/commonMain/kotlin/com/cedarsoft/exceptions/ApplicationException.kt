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


import com.cedarsoft.i18n.DefaultI18nConfiguration
import com.cedarsoft.i18n.I18nConfiguration

/**
 * Base class for all "expected" exceptions.
 * These are exceptions that can't be avoided - and we know already about it (e.g. network unavailable, invalid input)
 *
 * @noinspection AbstractClassWithoutAbstractMethods
 */
abstract class ApplicationException
/**
 * Creates a new ApplicationException including a cause (which is never presented to the user).
 *
 * @param cause            the cause
 * @param details          the details
 * @param parameters the (optional) message arguments
 * @param <T>              the details type
</T> */
protected constructor(
  cause: Throwable?,
  val details: Details,
  /**
   * The message arguments
   */
  val parameters: Map<String, Any>? = null
) : RuntimeException(cause) {

  /**
   * Creates a new ApplicationException without a cause
   *
   * @param details          the details (an enum that implements Details
   * @param parameters the (optional) message arguments
   * @param <T>              the details type
  </T> */
  protected constructor(
    details: Details,
    parameters: Map<String, Any>? = null
  ) : this(null, details, parameters)

  /**
   * Returns the error code
   */
  val errorCode: ErrorCode
    get() = details.errorCode

  /**
   * @return the message (based on the error code)
   */
  override val message: String?
    get() = details.errorCode.toString()

  /**
   * Returns the localized message for the given locale
   */
  fun getLocalizedMessage(i18nConfiguration: I18nConfiguration = DefaultI18nConfiguration): String {
    return details.getLocalizedMessage(i18nConfiguration, parameters)
  }

  /**
   * Returns the title for the given locale
   */
  fun getTitle(i18nConfiguration: I18nConfiguration = DefaultI18nConfiguration): String {
    return details.getTitle(i18nConfiguration, parameters)
  }

  /**
   * The details for the application exception
   */
  interface Details {
    /**
     * Returns the error code
     */
    val errorCode: ErrorCode

    /**
     * Returns the title
     */
    fun getTitle(i18nConfiguration: I18nConfiguration, parameters: Map<String, Any>?): String

    /**
     * Returns the localized message (without the error code)
     */
    fun getLocalizedMessage(i18nConfiguration: I18nConfiguration, parameters: Map<String, Any>?): String
  }
}
