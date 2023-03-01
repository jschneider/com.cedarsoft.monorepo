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
package it.neckar.open.exceptions.handling

import it.neckar.open.exceptions.ApplicationException
import it.neckar.open.exceptions.ErrorCode
import it.neckar.open.exceptions.ErrorCode.Companion.create
import it.neckar.open.i18n.I18nConfiguration
import javax.annotation.concurrent.Immutable

class MyTestException @JvmOverloads constructor(
  exceptionDetails: TestExceptionDetails,
  parameters: Map<String, Any>? = null,
) : ApplicationException(exceptionDetails, parameters) {
  /**
   */
  @Immutable
  enum class TestExceptionDetails(
    override val errorCode: ErrorCode,
  ) : Details {
    ERROR_1(701), ERROR_2(702);

    constructor(errorCode: Int) : this(create("TD", errorCode)) {}

    override fun getTitle(i18nConfiguration: I18nConfiguration, parameters: Map<String, Any>?): String {
      return "da title"
    }

    override fun getLocalizedMessage(i18nConfiguration: I18nConfiguration, parameters: Map<String, Any>?): String {
      return "A very long localized messages with a new line:\nThis is the next line!\nAnd another\n\nTwo new lines before"
    }
  }
}
