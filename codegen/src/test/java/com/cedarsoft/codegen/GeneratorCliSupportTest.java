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

package com.cedarsoft.codegen;

import org.jetbrains.annotations.NotNull;
import org.junit.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 *
 */
public class GeneratorCliSupportTest {
  @Test
  public void testIt() throws Exception {
    StringWriter out = new StringWriter();
    GeneratorCliSupport support = new GeneratorCliSupport( new AbstractGenerator() {
      @NotNull
      @Override
      protected String getRunnerClassName() {
        throw new UnsupportedOperationException();
      }
    }, "daCommand", new PrintWriter( out ) );

    support.run( new String[0] );
    assertEquals( "Missing required options: d, t\n" +
      "usage: daCommand -d <serializer dest dir> -t <test dest dir> path-to-class\n" +
      "-d,--destination <arg>     the output directory for the created classes\n" +
      "-h,--help                  display this use message\n" +
      "-t,--test-destination <arg>the output directory for the created tests", out.toString().trim() );
  }
}
