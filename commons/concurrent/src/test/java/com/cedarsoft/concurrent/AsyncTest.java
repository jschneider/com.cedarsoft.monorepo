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

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import com.cedarsoft.test.utils.ThreadExtension;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@ExtendWith(ThreadExtension.class)
public class AsyncTest {
  private ExecutorService executor;

  @BeforeEach
  public void setUp() throws Exception {
    executor = Executors.newSingleThreadExecutor();
  }

  @AfterEach
  public void tearDown() throws Exception {
    executor.shutdownNow();
    executor.awaitTermination(100, TimeUnit.MILLISECONDS);
  }

  @Disabled
  @Test
  void testException() throws InterruptedException {
    Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    try {
      AtomicReference<Throwable> caught = new AtomicReference<>();

      Thread.setDefaultUncaughtExceptionHandler((t, e) -> caught.set(e));

      Async async = new Async(executor);

      async.last(() -> {
        throw new IllegalArgumentException("Uups");
      });

      await().timeout(10, TimeUnit.SECONDS).untilAtomic(caught, new IsNot<>(new IsNull<>()));
    } finally {
      Thread.setDefaultUncaughtExceptionHandler(defaultUncaughtExceptionHandler);
    }
  }

  @Test
  public void basic() throws Exception {
    Async async = new Async(executor);

    AtomicBoolean otherJobsAdded = new AtomicBoolean();
    AtomicBoolean firstJobStarted = new AtomicBoolean();

    async.last("asdf", new Runnable() {
      @Override
      public void run() {
        firstJobStarted.set(true);

        await()
          .pollDelay(10, TimeUnit.MILLISECONDS)
          .until(otherJobsAdded::get);
      }
    });

    //Ensure the first stop has been started
    await()
      .pollDelay(10, TimeUnit.MILLISECONDS)
      .until(firstJobStarted::get);


    //Schedule other jobs that should be skipped, since there will be another job added last
    for (int i = 0; i < 10; i++) {
      async.last("asdf", new Runnable() {
        @Override
        public void run() {
          fail("Must not be called");
        }
      });
    }


    //Start the final job that is executed
    AtomicBoolean executed = new AtomicBoolean();
    async.last("asdf", new Runnable() {
      @Override
      public void run() {
        executed.set(true);
      }
    });

    //finish first job
    otherJobsAdded.set(true);

    await().pollDelay(10, TimeUnit.MILLISECONDS).until(executed::get);
  }
}
