/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

import com.cedarsoft.StillContainedException;
import org.testng.annotations.*;

import java.util.Comparator;

import static org.testng.Assert.*;

/**
 *
 */
public class DefaultRegistryTest {
  @Test
  public void testIt() {
    DefaultRegistry<String> registry = new DefaultRegistry<String>();
    assertEquals( registry.getStoredObjects().size(), 0 );

    registry.store( "1" );
    registry.store( "1" );
    assertEquals( registry.getStoredObjects().size(), 2 );
  }

  @Test
  public void testUnique() {
    DefaultRegistry<String> registry = new DefaultRegistry<String>( new Comparator<String>() {
      @Override
      public int compare( String o1, String o2 ) {
        return o1.compareTo( o2 );
      }
    } );
    assertEquals( registry.getStoredObjects().size(), 0 );

    registry.store( "1" );
    try {
      registry.store( "1" );
      fail( "Where is the Exception" );
    } catch ( StillContainedException ignore ) {
    }
    assertEquals( registry.getStoredObjects().size(), 1 );
  }
}
