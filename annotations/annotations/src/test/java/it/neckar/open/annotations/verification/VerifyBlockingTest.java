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
package it.neckar.open.annotations.verification;

import javax.annotation.Nullable;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.*;

import it.neckar.open.test.utils.CatchAllExceptionsExtension;
import it.neckar.open.test.utils.ThreadExtension;

/**
 */
@ExtendWith(ThreadExtension.class)
@ExtendWith(CatchAllExceptionsExtension.class)
public class VerifyBlockingTest {
  @Nullable
  private NotStuckVerifier.ThreadStuckEvaluator oldEvaluator;

  @BeforeEach
  public void setUp() throws Exception {
    oldEvaluator = NotStuckVerifier.getEvaluator();
    NotStuckVerifier.setEvaluator(new NotStuckVerifier.ExceptionThrowingEvaluator(10));
  }

  @AfterEach
  public void tearDown() throws Exception {
    NotStuckVerifier.setEvaluator(oldEvaluator);
  }

  @Test
  public void testItShouldFail() throws Exception {
    Assertions.assertThrows(IllegalThreadStateException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        NotStuckVerifier notStuckVerifier = NotStuckVerifier.start();
        callBlockingMethod();
        notStuckVerifier.finished();
      }
    });
  }

  @Test
  public void testItShouldWork() throws Exception {
    NotStuckVerifier notStuckVerifier = NotStuckVerifier.start();
    callNonBlockingMethod();
    notStuckVerifier.finished();
  }

  private void callNonBlockingMethod() {
  }

  private void callBlockingMethod() throws InterruptedException {
    Thread.sleep(1000L);
  }

}
