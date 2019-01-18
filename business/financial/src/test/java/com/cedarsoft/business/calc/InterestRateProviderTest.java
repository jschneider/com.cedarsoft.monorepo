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

package com.cedarsoft.business.calc;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;

/**
 *
 */
public class InterestRateProviderTest {
  private MappedInterestRateProvider provider;
  private static final double DEFAULT_RATE = 0.045;

  @BeforeEach
  public void setUp() throws Exception {
    provider = new MappedInterestRateProvider( DEFAULT_RATE );
  }

  @Test
  public void testDefaultRate() {
    assertEquals( DEFAULT_RATE, provider.getRate( new LocalDate() ), 0 );
  }

  @Test
  public void testPeriode() {
    provider.setRate( 0.06, new LocalDate( 2007, 1, 1 ) );
    provider.setRate( 0.07, new LocalDate( 2008, 1, 1 ) );

    assertEquals( DEFAULT_RATE, provider.getRate( new LocalDate( 2007, 1, 1 ).minusDays( 1 ) ), 0 );
    assertEquals( 0.06, provider.getRate( new LocalDate( 2007, 1, 1 ) ), 0 );
    assertEquals( 0.06, provider.getRate( new LocalDate( 2007, 1, 31 ) ), 0 );
    assertEquals( DEFAULT_RATE, provider.getRate( new LocalDate( 2007, 12, 31 ) ), 0 );
    assertEquals( 0.07, provider.getRate( new LocalDate( 2008, 1, 1 ) ), 0 );
    assertEquals( 0.07, provider.getRate( new LocalDate( 2008, 1, 31 ) ), 0 );
    assertEquals( DEFAULT_RATE, provider.getRate( new LocalDate( 2008, 2, 1 ) ), 0 );
  }

}
