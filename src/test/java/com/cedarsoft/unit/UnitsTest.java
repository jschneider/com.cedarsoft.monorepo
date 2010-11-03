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

package com.cedarsoft.unit;

import com.cedarsoft.unit.other.AM;
import com.cedarsoft.unit.si.A;
import com.cedarsoft.unit.si.C;
import com.cedarsoft.unit.si.K;
import com.cedarsoft.unit.si.degC;
import com.cedarsoft.unit.si.m;
import com.cedarsoft.unit.si.m2;
import com.cedarsoft.unit.si.m_s2;
import org.junit.*;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UnitsTest {
  @Test
  public void testSymbol() {
    assertEquals( "m", Units.getSymbol( m.class ) );
    assertEquals( "m²", Units.getSymbol( m2.class ) );
    assertEquals( "m/s²", Units.getSymbol( m_s2.class ) );
  }

  @Test
  public void testName() throws Exception {
    assertEquals( "metre", Units.getName( m.class ) );
    assertEquals( "square metre", Units.getName( m2.class ) );
    assertEquals( "metre per square second", Units.getName( m_s2.class ) );
  }

  @Test
  public void testSiBase() throws Exception {
    assertTrue( Units.isSiBaseUnit( m.class ) );
    assertTrue( Units.isSiBaseUnit( A.class ) );
    assertFalse( Units.isSiBaseUnit( degC.class ) );
    assertFalse( Units.isSiBaseUnit( C.class ) );
  }

  @Test
  public void testDerived() throws Exception {
    assertFalse( Units.isDerivedSiUnit( m.class ) );
    assertFalse( Units.isDerivedSiUnit( A.class ) );

    assertTrue( Units.isDerivedSiUnit( degC.class ) );
    assertTrue( Units.isDerivedSiUnit( C.class ) );
  }

  @Test
  public void testDefinition() throws Exception {
    assertEquals( 2, Units.getDefinitions( m.class ).size() );
    assertEquals( Arrays.asList( "optical path length through Earth's atmosphere", "relative to path length to that at the zenith at sea level" ), Units.getDefinitions( AM.class ) );
  }

  @Test
  public void testDerivedFrom() throws Exception {
    try {
      Units.getDerivedFrom( m.class );
      fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {
    }

    assertEquals( 1, Units.getDerivedFrom( degC.class ).size() );
    assertSame( K.class, Units.getDerivedFrom( degC.class ).get( 0 ) );
  }
}
