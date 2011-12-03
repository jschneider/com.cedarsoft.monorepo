/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cedarsoft.unit.utils;

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
    Assert.assertTrue( Units.isSiBaseUnit( m.class ) );
    Assert.assertTrue( Units.isSiBaseUnit( A.class ) );
    Assert.assertFalse( Units.isSiBaseUnit( degC.class ) );
    Assert.assertFalse( Units.isSiBaseUnit( C.class ) );
  }

  @Test
  public void testDerived() throws Exception {
    Assert.assertFalse( Units.isDerivedSiUnit( m.class ) );
    Assert.assertFalse( Units.isDerivedSiUnit( A.class ) );

    Assert.assertTrue( Units.isDerivedSiUnit( degC.class ) );
    Assert.assertTrue( Units.isDerivedSiUnit( C.class ) );
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
      Assert.fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {
    }

    assertEquals( 1, Units.getDerivedFrom( degC.class ).size() );
    Assert.assertSame( K.class, Units.getDerivedFrom( degC.class ).get( 0 ) );
  }
}
