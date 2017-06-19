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
package com.cedarsoft.test.utils;

import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Ignore
public class CatchAllExceptionsExtensionTest {
  @Rule
  public CatchAllExceptionsExtension catchAllExceptionsExtension = new CatchAllExceptionsExtension();

  @Test
  public void successfulTest() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new RuntimeException( "This one is ignored by JUnit!" );
      }
    } );
    thread.start();
    thread.join();
  }

  @Test
  public void ignoredAssertion() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        assertFalse(true); //will not be reported!
        throw new RuntimeException( "This one is ignored by JUnit!" );
      }
    } );
    thread.start();
    thread.join();
  }

  @Test
  public void testIt() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new IllegalStateException( "Hey, are u visible?" );
      }
    } );

    assertThat( thread.isAlive() ).isFalse();
    thread.start();
    thread.join();

    Thread thread2 = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new IllegalArgumentException( "Exception2" );
      }
    } );

    assertThat( thread2.isAlive() ).isFalse();
    thread2.start();
    thread2.join();
    assertThat( thread2.isAlive() ).isFalse();
  }

  @Test
  public void testFailing() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new IllegalStateException( "Hey, are u visible?" );
      }
    } );

    assertThat( thread.isAlive() ).isFalse();
    thread.start();
    thread.join();

    Thread thread2 = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new IllegalArgumentException( "Exception2" );
      }
    } );

    assertThat( thread2.isAlive() ).isFalse();
    thread2.start();
    thread2.join();
    assertThat( thread2.isAlive() ).isFalse();

    throw new UnsupportedOperationException( "asdf" );
  }
}
