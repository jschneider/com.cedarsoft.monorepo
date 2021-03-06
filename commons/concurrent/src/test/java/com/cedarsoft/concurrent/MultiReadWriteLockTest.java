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

package com.cedarsoft.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nullable;

import org.junit.jupiter.api.*;

import com.cedarsoft.commons.ThreadUtils;

/**
 *
 */
public class MultiReadWriteLockTest {
  @Test
  public void testEmpty() throws Exception {
    try {
      new MultiReadWriteLock();
      fail("Where is the Exception");
    }
    catch (IllegalArgumentException ignore) {
    }
  }

  @Test
  public void testWriteLock() throws Exception {
    final ReadWriteLock lock = new ReentrantReadWriteLock();
    MultiReadWriteLock multiLock = new MultiReadWriteLock( lock );

    multiLock.writeLock().lock();

    ThreadUtils.invokeInOtherThread(new Callable<Object>() {
      @Override
      @Nullable
      public Object call() throws Exception {
        assertFalse(lock.readLock().tryLock());
        assertFalse(lock.writeLock().tryLock());
        return null;
      }
    });

    multiLock.writeLock().unlock();

    ThreadUtils.invokeInOtherThread(new Callable<Object>() {
      @Override
      @Nullable
      public Object call() throws Exception {
        assertTrue(lock.readLock().tryLock());
        lock.readLock().unlock();
        assertTrue(lock.writeLock().tryLock());
        lock.writeLock().unlock();
        return null;
      }
    });
  }
}
