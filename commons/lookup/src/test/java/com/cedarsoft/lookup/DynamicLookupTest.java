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

package com.cedarsoft.lookup;

import org.junit.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

import static org.junit.Assert.*;

/**
 * <p>
 * Date: 07.10.2006<br>
 * Time: 17:57:45<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class DynamicLookupTest {
  @Test
  public void testRemove() {
    DynamicLookup lookup = new DynamicLookup();
    assertTrue( lookup.addValue( "asdf" ) );
    assertFalse( lookup.addValue( "asdf" ) );

    assertTrue( lookup.removeValue( "asdf" ) );
    assertFalse( lookup.removeValue( "asdf" ) );
  }

  @Test
  public void testRemove2() {
    DynamicLookup lookup = new DynamicLookup();

    assertTrue( lookup.addValue( 5 ) );
    assertEquals( 5, lookup.lookups().size() );
    assertTrue( lookup.addValue( 6 ) );

    assertEquals( 5, lookup.lookups().size() );

    assertTrue( lookup.removeValue( 1 ) );

    assertEquals( 0, lookup.lookups().size() );
  }

  @Test
  public void testRmove3() {
    DynamicLookup lookup = new DynamicLookup();

    assertTrue( lookup.addValue( 5 ) );
    assertEquals( 5, lookup.lookups().size() );
    assertTrue( lookup.removeValue( 1L ) );
    assertEquals( 1, lookup.lookups().size() );
    assertNotNull( lookup.lookup( Integer.class ) );
  }

  @Test
  public void testMultiple() {
    DynamicLookup lookup = Lookups.dynamicLookup( new AbstractAction( "myAction" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
        throw new UnsupportedOperationException();
      }
    } );

    assertEquals( 8, lookup.lookups().size() );
    assertNotNull( lookup.lookup( Action.class ) );
    assertNotNull( lookup.lookup( AbstractAction.class ) );
  }

  @Test
  public void testListeners() {
    DynamicLookup lookup = new DynamicLookup( "asdf" );
    LookupChangeListenerMock mock = new LookupChangeListenerMock();
    lookup.addChangeListener( mock );

    mock.addExpected( String.class, "asdf", "new" );
    mock.addExpected( Serializable.class, "asdf", "new" );
    mock.addExpected( Comparable.class, "asdf", "new" );
    mock.addExpected( CharSequence.class, "asdf", "new" );
    mock.addExpected( Object.class, "asdf", "new" );
    lookup.store( String.class, "new" );

    mock.verify();
  }

  @Test
  public void testIt() {
    DynamicLookup lookup = new DynamicLookup( "asdf" );
    assertFalse( lookup.lookups().isEmpty() );

    assertEquals( "asdf", lookup.lookup( String.class ) );
    assertEquals( "asdf", lookup.lookup( Object.class ) );
    assertNull( lookup.lookup( Integer.class ) );
    assertNull( lookup.lookup( List.class ) );
    assertEquals( "asdf", lookup.lookup( CharSequence.class ) );

    Map<Class<?>, Object> map = lookup.lookups();
    assertEquals( 5, map.size() );
  }

  @Test
  public void testInterfaces() {
    Object value = new ArrayList();
    DynamicLookup lookup = new DynamicLookup( value );

    assertNotNull( lookup.lookup( ArrayList.class ) );
    assertNotNull( lookup.lookup( AbstractList.class ) );
    assertNotNull( lookup.lookup( List.class ) );
    assertNotNull( lookup.lookup( RandomAccess.class ) );
    assertNotNull( lookup.lookup( Object.class ) );

    lookup.removeValue( value );

    assertNull( lookup.lookup( ArrayList.class ) );
    assertNull( lookup.lookup( AbstractList.class ) );
    assertNull( lookup.lookup( List.class ) );
    assertNull( lookup.lookup( RandomAccess.class ) );
    assertNull( lookup.lookup( Object.class ) );

    assertEquals( 0, lookup.lookups().size() );
  }
}

