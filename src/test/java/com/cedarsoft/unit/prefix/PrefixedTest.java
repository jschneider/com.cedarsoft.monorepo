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

package com.cedarsoft.unit.prefix;

import com.cedarsoft.unit.si.cm;
import com.cedarsoft.unit.si.m;
import com.cedarsoft.unit.si.mm;
import org.junit.*;
import org.junit.rules.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class PrefixedTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testIt() {
    assertEquals( 0.01, Prefixed.getFactor( centi.class ), 0 );
    assertEquals( 0.1, Prefixed.getFactor( deci.class ), 0 );
    assertEquals( 1000, Prefixed.getFactor( kilo.class ), 0 );
  }

  @Test
  public void testFactor2() {
    assertEquals( 0.01, Prefixed.getFactor( cm.class ), 0 );
    assertEquals( 0.001, Prefixed.getFactor( mm.class ), 0 );

    expectedException.expect( IllegalArgumentException.class );
    Prefixed.getFactor( m.class );
  }

  @Test
  public void testIsPrefixed() throws Exception {
    assertTrue( Prefixed.isPrefixed( cm.class ) );
    assertTrue( Prefixed.isPrefixed( mm.class ) );
    assertFalse( Prefixed.isPrefixed( m.class ) );
  }

  @Test
  public void testPrefix() throws Exception {
    assertNotNull( Prefixed.getPrefixAnnotation( cm.class ) );
    assertNotNull( Prefixed.getPrefixAnnotation( mm.class ) );
    assertNull( Prefixed.getPrefixAnnotation( m.class ) );
  }
}
