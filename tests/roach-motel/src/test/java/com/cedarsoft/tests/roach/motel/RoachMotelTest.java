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
package com.cedarsoft.tests.roach.motel;

import org.junit.*;


/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Ignore
public class RoachMotelTest {
  @Test
  public void it() throws Exception {

    for (int i = 0; i < 10000; i++) {
      RoachMotel roachMotel = new RoachMotel();

      Assert.assertEquals(0, roachMotel.a);
      Assert.assertFalse(roachMotel.called);
      Assert.assertEquals(0, roachMotel.c);


      new Thread(new Runnable() {
        @Override
        public void run() {
          roachMotel.doIt();
        }
      }).start();


      long start = System.nanoTime();

      while (true) {
        if (roachMotel.a == 0) {
          Assert.assertFalse(roachMotel.called);
          Assert.assertEquals(0, roachMotel.c);
        }

        if (roachMotel.called) {
          Assert.assertEquals(1, roachMotel.a);
        }

        if (roachMotel.c == 1) {
          Assert.assertEquals(1, roachMotel.a);
          Assert.assertTrue(roachMotel.called);
          break;
        }
      }

      System.out.println("Iteration took <" + (System.nanoTime() - start) + " ns>");
    }
  }

  /**
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  public static class RoachMotel {

    private static Object monitor = new Object();

    volatile int a;
    volatile boolean called;
    volatile int c;

    public void doIt() {
      a = 1;
      //synchronized (monitor) {
        called = true;
        someMethod();
      //}
      c = 1;
    }

    public boolean isCalled() {
      return called;
    }

    private static int someMethod() {
      int a = 0;
      for (int i = 0; i < 100000; i++) {
        a += i;
      }
      return a;
    }
  }
}