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

import com.cedarsoft.business.Money;
import com.cedarsoft.business.MutableMoney;
import com.cedarsoft.business.payment.DefaultPayment;
import com.cedarsoft.business.payment.Payment;
import org.joda.time.LocalDate;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class PangvBugTest {
  private List<Payment> payments;

  @Before
  public void setUp() throws Exception {
    payments = new ArrayList<Payment>();

    payments.add( new DefaultPayment( new Money( -29742.99 ), new LocalDate( 2007, 12, 07 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2008, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2009, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2010, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2011, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2012, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2013, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2014, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2015, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2016, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2017, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( -6324.37 ), new LocalDate( 2018, 1, 4 ) ) );
    payments.add( new DefaultPayment( new Money( 151196 ), new LocalDate( 2019, 1, 4 ) ) );
  }

  @Test
  public void testSum() {
    MutableMoney sum = new MutableMoney();
    for ( Payment payment : payments ) {
      sum.plus( payment.getAmount() );
    }
    assertEquals( 51884.94, sum.getEstimatedValue(), 0 );
  }

  @Test
  public void testIt() {
    PAngV2000Calculator calculator = new PAngV2000Calculator( new LocalDate( 2007, 12, 06 ) );
    calculator.addPayments( payments );
    assertEquals( 0.055, calculator.calculate().getRateOfReturnAsDouble(), 0.01 );
  }

}
