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

package com.cedarsoft.registry.hierarchy;

import com.cedarsoft.test.utils.MockitoTemplate;
import javax.annotation.Nonnull;

import org.junit.*;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 */
public class ChildDetectorTest {
  private CachingChildDetector<Class<?>, String> detector;

  @Before
  public void setUp() throws Exception {
    detector = new CachingChildDetector<Class<?>, String>() {
      @Nonnull
      @Override
      protected List<? extends String> createChildren( @Nonnull Class<?> parent ) {
        return Arrays.asList( parent.getPackage().getName(), parent.getName() );
      }
    };
  }

  @Test
  public void testListener() throws Exception {
    new MockitoTemplate() {
      @Mock
      private ChildChangeListener<Class<?>> listener;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        detector.addChangeListener( listener );
        detector.invalidateCache( String.class );
        detector.invalidateCache( Object.class );
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( listener ).notifyChildrenChangedFor( String.class );
        verify( listener ).notifyChildrenChangedFor( Object.class );
        verifyNoMoreInteractions( listener );
      }
    }.run();
  }

  @Test
  public void testIt() {
    List<? extends String> found = detector.findChildren( String.class );
    assertSame( found, detector.findChildren( String.class ) );
    assertSame( found, detector.findChildren( String.class ) );

    detector.invalidateCache( String.class );
    assertNotSame( found, detector.findChildren( String.class ) );
  }
}
