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
package it.neckar.open.exceptions

import it.neckar.open.i18n.I18nConfiguration
import it.neckar.open.i18n.TextService
import it.neckar.open.i18n.TextService.Companion.withFallback
import it.neckar.open.i18n.resolver.ResourceBundleTextResolver
import javax.annotation.Nonnull

class TestException : ApplicationException {
  constructor(cause: Throwable?, details: Details, parameters: Map<String, Any>?) : super(cause, details, parameters)
  constructor(details: Details, parameters: Map<String, Any>?) : super(details, parameters)

  enum class TestExceptionDetails(
    override val errorCode: ErrorCode
  ) : Details {

    ERROR_1(701),
    ERROR_2(702);

    @Nonnull
    private val textService: TextService = withFallback(ResourceBundleTextResolver("it.neckar.open.exceptions.testmessages"))

    constructor(errorCode: Int) : this(ErrorCode(PREFIX, errorCode)) {}

    override fun getTitle(i18nConfiguration: I18nConfiguration, parameters: Map<String, Any>?): String {
      return textService[this, i18nConfiguration, CATEGORY_TITLE, parameters]
    }

    override fun getLocalizedMessage(i18nConfiguration: I18nConfiguration, parameters: Map<String, Any>?): String {
      return textService[this, i18nConfiguration, null, parameters]
    }

    companion object {
      const val CATEGORY_TITLE: String = "title"
    }
  }

  companion object {
    @Nonnull
    val PREFIX: ErrorCode.Prefix = ErrorCode.Prefix("TD")
  }
}
