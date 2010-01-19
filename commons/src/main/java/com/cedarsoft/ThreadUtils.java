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

package com.cedarsoft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Offers static thread utils
 */
public class ThreadUtils {
  public static boolean isEventDispatchThread() {
    return SwingUtilities.isEventDispatchThread();
  }

  public static void assertEventDispatchThread() throws IllegalThreadStateException {
    if ( !isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "Not in EDT" );
    }
  }

  public static void assertNotEventDispatchThread() throws IllegalThreadStateException {
    if ( isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "Is EDT" );
    }
  }

  @Nullable
  public static <T> T inokeInOtherThread( @NotNull Callable<T> callable ) throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      Future<T> future = executor.submit( callable );
      return future.get();
    } finally {
      executor.shutdown();
    }
  }

  /**
   * Invokes the runnable within the EDT
   */
  public static void invokeInEventDispatchThread( @NotNull Runnable runnable ) {
    if ( isEventDispatchThread() ) {
      runnable.run();
    } else {
      try {
        SwingUtilities.invokeAndWait( runnable );
      } catch ( InterruptedException e ) {
        throw new RuntimeException( e );
      } catch ( InvocationTargetException e ) {
        throw new RuntimeException( e );
      }
    }
  }
}
