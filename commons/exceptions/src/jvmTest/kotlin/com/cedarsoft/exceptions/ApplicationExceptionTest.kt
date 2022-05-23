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

import assertk.*
import assertk.assertions.*
import com.cedarsoft.i18n.I18nConfiguration
import org.junit.jupiter.api.Test
import java.util.ResourceBundle
import javax.annotation.Nonnull

/**
 *
 */
class ApplicationExceptionTest {
  @Test
  fun testSetup() {
    assertThat(ResourceBundle.getBundle(BUNDLE)).isNotNull()
  }

  @Test
  fun testDefaultLocale() {
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_1, mapOf("myParam" to "a")).getLocalizedMessage(I18nConfiguration.US)).isEqualTo("The Value 1 en: <a>")
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_1, mapOf("myParam" to "a")).getLocalizedMessage(I18nConfiguration.US)).isEqualTo("The Value 1 en: <a>")
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_1, mapOf("myParam" to "b")).message).isEqualTo("TD-701")
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_2, mapOf("myParam" to "c")).getLocalizedMessage(I18nConfiguration.US)).isEqualTo("The Value 2 en: <c>")
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_2, mapOf("myParam" to "d")).message).isEqualTo("TD-702")
  }

  @Test
  fun testLocales() {
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_1, mapOf("myParam" to "asdf")).getLocalizedMessage(I18nConfiguration.Germany)).isEqualTo("The Value 1 de: <asdf>")
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_1, mapOf("myParam" to "asdf")).message).isEqualTo("TD-701")
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_2, mapOf("myParam" to "asdf")).getLocalizedMessage(I18nConfiguration.US)).isEqualTo("The Value 2 en: <asdf>")
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_2, mapOf("myParam" to "asdf")).getLocalizedMessage(I18nConfiguration.Germany)).isEqualTo("The Value 2 de: <asdf>")
    assertThat(TestException(TestException.TestExceptionDetails.ERROR_2, mapOf("myParam" to "asdf")).message).isEqualTo("TD-702")
  }

  companion object {
    @Nonnull
    val BUNDLE: String = "com.cedarsoft.exceptions.testmessages"
  }
}
