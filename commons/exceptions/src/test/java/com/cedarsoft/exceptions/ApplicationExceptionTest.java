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
package com.cedarsoft.exceptions;


import static org.assertj.core.api.Assertions.*;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ApplicationExceptionTest {
  @Nonnull
  public static final String BUNDLE = "com.cedarsoft.exceptions.testmessages";

  @Test
  public void testSetup() throws Exception {
    assertThat( ResourceBundle.getBundle( BUNDLE ) ).isNotNull();
  }

  @Test
  public void testDefaultLocale() throws Exception {
    Locale.setDefault( Locale.ENGLISH );

    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_1, "a" ).getLocalizedMessage() ).isEqualTo("The Value 1 en: <a>" );
    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_1, "b" ).getMessage() ).isEqualTo("TD-701" );

    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_2, "c" ).getLocalizedMessage() ).isEqualTo("The Value 2 en: <c>" );
    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_2, "d" ).getMessage() ).isEqualTo("TD-702" );
  }

  @Test
  public void testLocales() throws Exception {
    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_1, "asdf" ).getLocalizedMessage(Locale.ENGLISH ) ).isEqualTo("The Value 1 en: <asdf>" );
    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_1, "asdf" ).getLocalizedMessage(Locale.GERMAN ) ).isEqualTo("The Value 1 de: <asdf>" );
    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_1, "asdf" ).getMessage() ).isEqualTo("TD-701" );
    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_2, "asdf" ).getLocalizedMessage(Locale.ENGLISH ) ).isEqualTo("The Value 2 en: <asdf>" );
    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_2, "asdf" ).getLocalizedMessage(Locale.GERMAN ) ).isEqualTo("The Value 2 de: <asdf>" );
    assertThat( new TestException(TestException.TestExceptionDetails.ERROR_2, "asdf" ).getMessage() ).isEqualTo("TD-702" );
  }

}
