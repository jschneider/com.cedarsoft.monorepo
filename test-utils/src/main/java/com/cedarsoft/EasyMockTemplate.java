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

package com.cedarsoft;

import net.sf.cglib.proxy.Enhancer;
import org.jetbrains.annotations.NotNull;

import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

/**
 * <p>Abstract EasyMockTemplate class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class EasyMockTemplate {

  /**
   * Mock objects managed by this template
   */
  @NotNull
  private final List<Object> mocks = new ArrayList<Object>();

  /**
   * Constructor.
   *
   * @param mocks the mocks for this template to manage.
   * @throws IllegalArgumentException
   *          if the list of mock objects is <code>null</code> or empty.
   */
  protected EasyMockTemplate( @NotNull Object... mocks ) {
    if ( mocks.length == 0 ) {
      throw new IllegalArgumentException( "The list of mock objects should not be empty" );
    }
    for ( Object mock : mocks ) {
      if ( mock == null ) {
        throw new IllegalArgumentException( "The list of mocks should not include null values" );
      }
      this.mocks.add( checkAndReturnMock( mock ) );
    }
  }

  private static Object checkAndReturnMock( @NotNull Object mock ) {
    if ( Enhancer.isEnhanced( mock.getClass() ) ) {
      return mock;
    }
    if ( Proxy.isProxyClass( mock.getClass() ) ) {
      return mock;
    }
    throw new IllegalArgumentException( mock + " is not a mock" );
  }

  /**
   * Encapsulates EasyMock's behavior pattern.
   * <ol>
   * <li>Set up expectations on the mock objects</li>
   * <li>Set the state of the mock controls to "replay"</li>
   * <li>Execute the code to test</li>
   * <li>Verify that the expectations were met</li>
   * </ol>
   * Steps 2 and 4 are considered invariant behavior while steps 1 and 3 should be implemented by subclasses of this
   * template.
   *
   * @throws Exception if any.
   */
  public final void run() throws Exception {
    setUp();
    expectations();
    for ( Object mock : mocks ) {
      replay( mock );
    }
    codeToTest();
    for ( Object mock : mocks ) {
      verify( mock );
    }
  }

  /**
   * Sets the expectations on the mock objects.
   *
   * @throws Exception if any.
   */
  protected abstract void expectations() throws Exception;

  /**
   * Executes the code that is under test.
   *
   * @throws Exception if any.
   */
  protected abstract void codeToTest() throws Exception;

  /**
   * Sets up the test fixture if necessary.
   */
  public void setUp() {
  }
}
