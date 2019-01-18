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

package com.cedarsoft.business.calc.roi;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;

import com.cedarsoft.business.Money;
import com.cedarsoft.business.payment.DefaultPayment;
import com.cedarsoft.business.payment.Payment;

/**
 *
 */
public class PangVBug2Test {
  private List<Payment> payments;

  @BeforeEach
  public void setUp() throws Exception {
    payments = new ArrayList<Payment>();

    payments.add( new DefaultPayment( new Money( -770720.89 ), new LocalDate( 2008, 4, 23 ) ) );
    payments.add( new DefaultPayment( new Money( 1149475 ), new LocalDate( 2015, 12, 1 ) ) );
  }

  @Test
  public void testIt() {
    PAngV2000Calculator calculator = new PAngV2000Calculator( new LocalDate( 2008, 4, 23 ) );
    calculator.addPayments( payments );
    assertEquals( 0.055, calculator.calculate().getRateOfReturnAsDouble(), 0.01 );
  }
}
