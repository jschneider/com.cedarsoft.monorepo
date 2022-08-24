/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package com.cedarsoft.commons

import com.cedarsoft.commons.ThreadUtils.assertEventDispatchThread
import com.cedarsoft.commons.ThreadUtils.assertNotEventDispatchThread
import com.cedarsoft.commons.ThreadUtils.invokeInEventDispatchThread
import com.cedarsoft.commons.ThreadUtils.invokeInOtherThread
import com.cedarsoft.commons.ThreadUtils.isEventDispatchThread
import com.cedarsoft.commons.ThreadUtils.waitForEventDispatchThread
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ExecutionException

/**
 *
 */
class ThreadUtilsTest {
  @Test
  fun testIt() {
    assertNotEventDispatchThread()
    Assertions.assertThrows(InvocationTargetException::class.java) { invokeInEventDispatchThread(Runnable { assertNotEventDispatchThread() }) }
  }

  @Test
  fun testEdt() {
    Assertions.assertThrows(IllegalThreadStateException::class.java) { assertEventDispatchThread() }
  }

  @Test
  fun testInvoke() {
    val called = booleanArrayOf(false)
    invokeInEventDispatchThread {
      called[0] = true
      Assert.assertTrue(isEventDispatchThread)
      assertEventDispatchThread()
    }
    waitForEventDispatchThread()
    Assert.assertTrue(called[0])
  }

  @Test
  @Throws(ExecutionException::class, InterruptedException::class)
  fun testOther() {
    val called = booleanArrayOf(false)
    Assert.assertEquals("asdf", invokeInOtherThread<Any> {
      called[0] = true
      Assert.assertFalse(isEventDispatchThread)
      assertNotEventDispatchThread()
      "asdf"
    })
    Assert.assertTrue(called[0])
  }
}
