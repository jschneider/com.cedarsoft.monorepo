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

import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This verifier may be used to verify that a thread is not stuck.
 * This is useful in combination with the com.cedarsoft.annotations.NonBlocking annotation.
 *
 */
public class NotStuckVerifier {
  @Nonnull
  public static NotStuckVerifier start() {
    return new NotStuckVerifier(System.currentTimeMillis());
  }

  private final long startTime;

  public NotStuckVerifier(long startTime) {
    this.startTime = startTime;
  }

  /**
   * Must be called after the method has returned.
   * Will interrupt the monitoring
   */
  public void finished() {
    ThreadStuckEvaluator evaluator = getEvaluator();
    if (evaluator != null) {
      evaluator.evaluate(System.currentTimeMillis() - startTime);
    }
  }

  /**
   * Sets the threadStuckEvaluator
   *
   * @param threadStuckEvaluator the threadStuckEvaluator
   */
  public static void setEvaluator(@Nullable ThreadStuckEvaluator threadStuckEvaluator) {
    NotStuckVerifier.EVALUATOR_REF.set(threadStuckEvaluator);
  }

  @Nullable
  public static ThreadStuckEvaluator getEvaluator() {
    return EVALUATOR_REF.get();
  }

  @Nonnull
  private static final AtomicReference<ThreadStuckEvaluator> EVALUATOR_REF = new AtomicReference<ThreadStuckEvaluator>();

  public interface ThreadStuckEvaluator {
    /**
     * Evaluates whether the thread has been stuck.
     * This method should throw an exception or notify somebody about stuck threads.
     *
     * @param millis the time in millis the method took
     */
    void evaluate(long millis);
  }

  /**
   * Evaluator that throws an {@link java.lang.IllegalThreadStateException} if the
   * max delay has been reached
   */
  public static class ExceptionThrowingEvaluator implements ThreadStuckEvaluator {
    public final int maxDelay;

    public ExceptionThrowingEvaluator() {
      this(10);
    }

    public ExceptionThrowingEvaluator(int maxDelay) {
      this.maxDelay = maxDelay;
    }

    @Override
    public void evaluate(long millis) {
      if (millis > maxDelay) {
        throw new IllegalThreadStateException("Thread stuck in method marked as non blocking for " + millis + " ms");
      }
    }
  }
}
