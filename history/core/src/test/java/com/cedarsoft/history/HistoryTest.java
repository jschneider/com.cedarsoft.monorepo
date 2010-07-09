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

package com.cedarsoft.history;

import org.joda.time.LocalDate;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class HistoryTest {
  private LocalDate begin;
  private LocalDate end;
  private History<DefaultHistoryEntry> history;
  private LocalDate middle;

  @Before
  protected void setUp() throws Exception {
    begin = new LocalDate( 2007, 04, 06 );
    end = new LocalDate( 2007, 8, 01 );
    middle = new LocalDate( 2007, 7, 01 );
    history = new DefaultHistory<DefaultHistoryEntry>();
  }

  @After
  protected void tearDown() throws Exception {

  }

  @Test
  public void testDelete() {
    assertEquals( 0, history.getEntries().size() );
    DefaultHistoryEntry first = new DefaultHistoryEntry( begin );
    history.addEntry( first );
    assertEquals( 1, history.getEntries().size() );
    history.removeEntry( first );
    assertEquals( 0, history.getEntries().size() );
    try {
      history.getLatestEntry();
      fail( "Where is the Exception" );
    } catch ( NoValidElementFoundException e ) {
    }
  }

  @Test
  public void testChange() {
    assertEquals( 0, history.getEntries().size() );

    DefaultHistoryEntry first = new DefaultHistoryEntry( begin );
    history.addEntry( first );
    DefaultHistoryEntry second = new DefaultHistoryEntry( middle );
    history.addEntry( second );
    DefaultHistoryEntry third = new DefaultHistoryEntry( end );
    history.addEntry( third );

    assertSame( third, history.getLatestEntry() );
  }

  @Test
  public void testIt() {
    assertEquals( 0, history.getEntries().size() );
    DefaultHistoryEntry first = new DefaultHistoryEntry( begin );
    history.addEntry( first );
    DefaultHistoryEntry second = new DefaultHistoryEntry( middle );
    history.addEntry( second );
    DefaultHistoryEntry third = new DefaultHistoryEntry( end );
    history.addEntry( third );

    assertEquals( 3, history.getEntries().size() );
    assertSame( third, history.getLatestEntry() );
  }

  @Test
  public void testWrongOrder() {
    assertEquals( 0, history.getEntries().size() );
    history.addEntry( new DefaultHistoryEntry( middle ) );
    history.addEntry( new DefaultHistoryEntry( begin ) );
    history.addEntry( new DefaultHistoryEntry( end ) );
    assertEquals( 3, history.getEntries().size() );
  }
}
