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
package com.cedarsoft.app.xdg;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.*;

import com.google.common.base.StandardSystemProperty;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class XdgUtilDemo {
  @Test
  public void testIt() throws Exception {
    String value = StandardSystemProperty.OS_NAME.value();
    if ( !value.contains( "Linux" ) ) {
      System.err.println( "Not running on Linux!" );
      return;
    }

    System.err.println( "Config Home: " + XdgUtil.getConfigHome().getAbsolutePath() );
    System.err.println( "Cache Home: " + XdgUtil.getCacheHome().getAbsolutePath() );
    System.err.println( "Data Home: " + XdgUtil.getDataHome().getAbsolutePath() );
  }

  @Disabled
  @Test
  public void testFields() throws Exception {
    System.out.println("Fields --------------");
    for (Field field : XdgUtil.class.getDeclaredFields()) {
      System.out.println("Field: " + field);
    }

    System.out.println("Methods --------------");
    for (Method method : XdgUtil.class.getDeclaredMethods()) {
      System.out.println("Method: " + method);
    }
  }
}
