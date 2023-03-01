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

package it.neckar.open.business.calc.roi;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;

import it.neckar.open.business.Money;
import it.neckar.open.business.MutableMoney;
import it.neckar.open.business.payment.DefaultPayment;
import it.neckar.open.business.payment.Payment;

/**
 *
 */
public class PangVOtherButTest {
  private List<Payment> payments;

  @BeforeEach
  public void setUp() throws Exception {
    payments = new ArrayList<Payment>();

    payments.add(new DefaultPayment(new Money(-1000), new LocalDate(2003, 12, 22)));
    payments.add(new DefaultPayment(new Money(500), new LocalDate(2004, 1, 6)));
    payments.add( new DefaultPayment( new Money( -50 ), new LocalDate( 2004, 1, 26 ) ) );
    payments.add( new DefaultPayment( new Money( -50 ), new LocalDate( 2005, 1, 26 ) ) );
    payments.add( new DefaultPayment( new Money( 1100 ), new LocalDate( 2006, 1, 11 ) ) );
  }

  @Test
  public void testSum() {
    MutableMoney sum = new MutableMoney();
    for ( Payment payment : payments ) {
      sum.plus( payment.getAmount() );
    }
    assertEquals( 500.0, sum.getEstimatedValue(), 0 );
  }

  @Test
  public void testIt() {
    PAngV2000Calculator calculator = new PAngV2000Calculator( new LocalDate( 2003, 12, 22 ) );
    calculator.addPayments( payments );
    PangV pangv = calculator.calculate();
    assertEquals( 0.353378, pangv.getRateOfReturnAsDouble(), 0.01 );
  }

}
