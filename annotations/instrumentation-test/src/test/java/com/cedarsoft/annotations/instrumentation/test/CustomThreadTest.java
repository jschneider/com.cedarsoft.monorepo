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
package com.cedarsoft.annotations.instrumentation.test;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import com.cedarsoft.annotations.verification.DelegatingThreadVerificationStrategy;
import com.cedarsoft.annotations.verification.ThreadVerificationStrategy;
import com.cedarsoft.annotations.verification.VerifyThread;
import com.cedarsoft.test.utils.CatchAllExceptionsExtension;
import com.google.common.collect.ImmutableList;

/**
 */
@ExtendWith(CatchAllExceptionsExtension.class)
public class CustomThreadTest {

  @BeforeEach
  public void setUp() throws Exception {
    VerifyThread.setStrategy( new ThreadVerificationStrategy() {
      @Override
      public void verifyThread( @Nonnull String... threadDescriptors ) {
        Set<String> descriptorsSet = new HashSet<String>( Arrays.asList( threadDescriptors ) );
        if ( !descriptorsSet.contains( Thread.currentThread().getName() ) ) {
          throw new IllegalStateException( "Invalid thread access from <" + Thread.currentThread().getName() + ">" );
        }
      }
    } );
  }

  @AfterEach
  public void tearDown() throws Exception {
    VerifyThread.setStrategy( new DelegatingThreadVerificationStrategy( ImmutableList.<ThreadVerificationStrategy>of() ) );
  }

  @Test
  public void testCustomThreadAnnotFail() throws Exception {
    RunnerClass runnerClass = new RunnerClass();
    try {
      runnerClass.methodCustomThread();
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
      Assertions.assertThat(e ).hasMessage("Invalid thread access from <main>" );
    }
  }

  @Test
  public void testCustomThreadAnnot() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        RunnerClass runnerClass = new RunnerClass();
        runnerClass.methodCustomThread();
      }
    }, "my-custom-thread" );
    thread.start();
    thread.join();
  }


  @Test
  public void testCustomThreadAnnotTest() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        daMethodCalledInCustomThread();
      }
    }, "my-custom-thread" );
    thread.start();
    thread.join();
  }

  @CustomThreadAnnotation
  public void daMethodCalledInCustomThread() {
    System.out.println( "com.cedarsoft.annotations.instrumentation.test.RunnerClassTest.daMethodCalledInCustomThread" );
  }
}
