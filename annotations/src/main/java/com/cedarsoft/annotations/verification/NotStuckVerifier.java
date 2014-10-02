package com.cedarsoft.annotations.verification;

import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This verifier may be used to verify that a thread is not stuck.
 * This is useful in combination with the com.cedarsoft.annotations.NonBlocking annotation.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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
