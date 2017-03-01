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

package com.cedarsoft.registry;

import org.junit.*;

import java.util.Comparator;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 *
 */
public class TreeSetTest {
  @Test
  public void testIt() throws Exception {
    TreeSet<MyObject> set = new TreeSet<MyObject>( new Comparator<MyObject>() {
      @Override
      public int compare( MyObject o1, MyObject o2 ) {
        return Integer.valueOf( o1.id ).compareTo( o2.id );
      }
    } );

    set.add( new MyObject( 5 ) );
    set.add( new MyObject( 4 ) );
    set.add( new MyObject( 3 ) );

    assertEquals( 3, set.size() );

    assertTrue( set.contains( new MyObject( 3 ) ) );
  }

  public static class MyObject {
    private final int id;

    public MyObject( int id ) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

    @Override
    public boolean equals( Object obj ) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
      throw new UnsupportedOperationException();
    }
  }
}
