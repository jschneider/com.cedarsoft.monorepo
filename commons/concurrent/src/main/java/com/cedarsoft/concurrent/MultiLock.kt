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

import java.util.ArrayList
import java.util.Arrays
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock

/**
 * USE WITH CARE!!!
 * May easily result in dead locks
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class MultiLock
/**
 *
 * Constructor for MultiLock.
 *
 * @param locks a Collection object.
 */
constructor(
  locks: Collection<Lock>
) : Lock {
  private val locks = ArrayList<Lock>()

  /**
   *
   * Constructor for MultiLock.
   *
   * @param locks a Lock object.
   */
  constructor(vararg locks: Lock) : this(Arrays.asList<Lock>(*locks)) {}

  init {
    this.locks.addAll(locks)
  }

  /**
   * {@inheritDoc}
   */
  override fun lock() {
    for (lock in locks) {

      lock.lock()
    }
  }

  /**
   * {@inheritDoc}
   */
  @Throws(InterruptedException::class)
  override fun lockInterruptibly() {
    throw UnsupportedOperationException("Not supported for a multi lock")
  }

  /**
   * {@inheritDoc}
   */
  override fun tryLock(): Boolean {
    throw UnsupportedOperationException("Not supported for a multi lock")
  }

  /**
   * {@inheritDoc}
   */
  @Throws(InterruptedException::class)
  override fun tryLock(time: Long, unit: TimeUnit): Boolean {
    throw UnsupportedOperationException("Not supported for a multi lock")
  }

  /**
   * {@inheritDoc}
   */
  override fun unlock() {
    for (lock in locks) {
      lock.unlock()
    }
  }

  /**
   * {@inheritDoc}
   */
  override fun newCondition(): Condition {
    throw UnsupportedOperationException("Cannot create a condition for a multi lock")
  }
}
