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
package com.cedarsoft.unit.utils.prefix;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import com.cedarsoft.unit.prefix.centi;
import com.cedarsoft.unit.prefix.deci;
import com.cedarsoft.unit.prefix.kilo;
import com.cedarsoft.unit.si.cm;
import com.cedarsoft.unit.si.m;
import com.cedarsoft.unit.si.mm;

/**
 */
@Disabled
public class PrefixedTest {
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

    try {
      Prefixed.getFactor(m.class);
      fail("Where is the Exception");
    }
    catch (IllegalArgumentException e) {

    }
  }

  @Test
  public void testIsPrefixed() throws Exception {
    Assert.assertTrue( Prefixed.isPrefixed( cm.class ) );
    Assert.assertTrue(Prefixed.isPrefixed(mm.class));
    Assert.assertFalse( Prefixed.isPrefixed( m.class ) );
  }

  @Test
  public void testPrefix() throws Exception {
    Assert.assertNotNull( Prefixed.getPrefixAnnotation( cm.class ) );
    Assert.assertNotNull( Prefixed.getPrefixAnnotation( mm.class ) );
    Assert.assertNull( Prefixed.getPrefixAnnotation( m.class ) );
  }
}
