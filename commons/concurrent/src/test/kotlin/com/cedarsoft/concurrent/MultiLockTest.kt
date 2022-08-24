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
package com.cedarsoft.concurrent

import com.cedarsoft.commons.ThreadUtils.invokeInOtherThread
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 *
 */
class MultiLockTest {
  private lateinit var lock0: Lock
  private lateinit var lock1: Lock
  private lateinit var multiLock: MultiLock

  @BeforeEach
  fun setUp() {
    lock0 = ReentrantLock()
    lock1 = ReentrantLock()
    multiLock = MultiLock(lock0, lock1)
  }

  @AfterEach
  @Throws(Exception::class)
  fun tearDown() {
  }

  @Test
  @Throws(ExecutionException::class, InterruptedException::class)
  fun testBasic() {
    multiLock.lock()

    invokeInOtherThread(Callable<Any> {
      Assert.assertFalse(lock0.tryLock())
      Assert.assertFalse(lock1.tryLock())
    })

    multiLock.unlock()
  }
}
