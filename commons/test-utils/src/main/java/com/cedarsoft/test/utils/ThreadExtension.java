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

import com.google.common.base.Joiner;
import org.junit.jupiter.api.extension.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ThreadExtension implements BeforeEachCallback, AfterEachCallback {
  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    before();
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    if (extensionContext.getExecutionException().isPresent()) {
      afterFailing();
      return;
    }

    after();
  }

  public static final String STACK_TRACE_ELEMENT_SEPARATOR = "\n\tat ";

  @Nullable
  private final ThreadMatcher ignoredThreadMatcher;

  public ThreadExtension() {
    this( new DefaultThreadMatcher() );
  }

  public ThreadExtension(@Nullable ThreadMatcher ignoredThreadMatcher ) {
    this.ignoredThreadMatcher = ignoredThreadMatcher;
  }

  @Nullable
  private Collection<Thread> initialThreads;

  private void before() {
    if ( initialThreads != null ) {
      throw new IllegalStateException("initialThreads is not null");
    }

    initialThreads = Thread.getAllStackTraces().keySet();
  }

  @Nonnull
  public Collection<? extends Thread> getInitialThreads() {
    if ( initialThreads == null ) {
      throw new IllegalStateException( "not initialized yet" );
    }
    return Collections.unmodifiableCollection( initialThreads );
  }

  private void afterFailing() {
    Set<? extends Thread> remainingThreads = getRemainingThreads();
    if ( !remainingThreads.isEmpty() ) {
      System.err.print( "Some threads have been left:\n" + buildMessage( remainingThreads ) );
    }

    initialThreads = null;
  }

  private void after() {
    try {
      Set<? extends Thread> remainingThreads = getRemainingThreads();
      if (!remainingThreads.isEmpty()) {
        System.err.println("--> " + "Some threads have been left:\n" + buildMessage(remainingThreads));
        throw new IllegalStateException("Some threads have been left:\n" + buildMessage(remainingThreads));
      }
    } finally {
      initialThreads = null;
    }
  }

  @Nonnull
  public Set<? extends Thread> getRemainingThreads() {
    if (initialThreads == null) {
      throw new IllegalStateException("initialThreads is null");
    }
    Collection<Thread> threadsNow = Thread.getAllStackTraces().keySet();

    Set<Thread> remainingThreads = new HashSet<Thread>( threadsNow );
    remainingThreads.removeAll( initialThreads );

    for ( Iterator<Thread> iterator = remainingThreads.iterator(); iterator.hasNext(); ) {
      Thread remainingThread = iterator.next();
      if ( !remainingThread.isAlive() ) {
        iterator.remove();
        continue;
      }

      //Ignore the threads
      if ( this.ignoredThreadMatcher != null && ignoredThreadMatcher.shallIgnore( remainingThread ) ) {
        iterator.remove();
        continue;
      }

      //Wait for a little bit, sometimes the threads die off
      for (int i = 0; i < 10; i++) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException ignore) {
        }

        //Second try
        if (!remainingThread.isAlive()) {
          iterator.remove();
          break;
        }
      }
    }
    return remainingThreads;
  }

  @Nonnull
  private String buildMessage( @Nonnull Set<? extends Thread> remainingThreads ) {
    StringBuilder builder = new StringBuilder();

    builder.append( "// Remaining Threads:" ).append( "\n" );
    builder.append( "-----------------------" ).append( "\n" );
    for ( Thread remainingThread : remainingThreads ) {
      builder.append( "---" );
      builder.append( "\n" );
      builder.append( remainingThread );
      builder.append( STACK_TRACE_ELEMENT_SEPARATOR );
      builder.append( Joiner.on( STACK_TRACE_ELEMENT_SEPARATOR ).join( remainingThread.getStackTrace() ) );
      builder.append( "\n" );
    }
    builder.append( "-----------------------" ).append( "\n" );

    return builder.toString();
  }

  public interface ThreadMatcher {
    boolean shallIgnore( @Nonnull Thread remainingThread );
  }

  /**
   * Default implementation that ignore several known threads.
   */
  public static class DefaultThreadMatcher implements ThreadMatcher {
    @Override
    public boolean shallIgnore( @Nonnull Thread remainingThread ) {
      @Nullable ThreadGroup threadGroup = remainingThread.getThreadGroup();
      if ( threadGroup == null ) {
        //this means the thread has died
        return true;
      }
      String threadGroupName = threadGroup.getName();
      String threadName = remainingThread.getName();

      if ((threadGroupName.equals("system") &&
        threadName.equals("Keep-Alive-Timer"))
        ||
        (threadGroupName.equals("system") &&
          threadName.equals("process reaper"))
        ||
        (threadGroupName.equals("system") &&
          threadName.equals("Keep-Alive-SocketCleaner"))
        ||
        (threadGroupName.equals("system") &&
          threadName.equals("Java2D Disposer"))
        ||
        threadName.startsWith("AWT-")
        ||
        (threadGroupName.equals("main") &&
          threadName.startsWith("QuantumRenderer"))
        ) {
        return true;
      }

      //Special check for awaitility - this lib leaves one thread open for about 100ms
      for (StackTraceElement stackTraceElement : remainingThread.getStackTrace()) {
        if (stackTraceElement.getClassName().equals("org.awaitility.core.ConditionAwaiter$1")) {
          if (stackTraceElement.getMethodName().equals("run")) {
            return true;
          }
        }
      }

      return false;
    }
  }
}
