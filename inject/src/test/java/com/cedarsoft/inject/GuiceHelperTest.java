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

package com.cedarsoft.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.util.Types;
import org.testng.annotations.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

/**
 *
 */
public class GuiceHelperTest {
  @Test
  public void testBindCollections() {
    AbstractModule module = new AbstractModule() {
      @Override
      protected void configure() {
        bind( ( ( Key<Set<String>> ) Key.get( Types.setOf( String.class ) ) ) ).toInstance( new HashSet<String>() );
        GuiceHelper.bindWildcardCollectionForSet( binder(), String.class );
      }
    };

    Guice.createInjector( module );
  }

  @Test
  public void testIt() {
    assertEquals( GuiceHelper.superCollectionOf( String.class ).toString(), "java.util.Collection<? extends java.lang.String>" );
    assertEquals( GuiceHelper.superListOf( String.class ).toString(), "java.util.List<? extends java.lang.String>" );
  }

  @Test
  public void testGuice() {
    assertEquals( Types.listOf( String.class ).toString(), "java.util.List<java.lang.String>" );
    assertEquals( Types.listOf( Types.subtypeOf( String.class ) ).toString(), "java.util.List<? extends java.lang.String>" );
    assertEquals( Types.newParameterizedType( Collection.class, Types.subtypeOf( String.class ) ).toString(), "java.util.Collection<? extends java.lang.String>" );
  }
}
