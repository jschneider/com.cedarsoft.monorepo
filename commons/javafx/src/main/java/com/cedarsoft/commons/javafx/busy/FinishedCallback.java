package com.cedarsoft.commons.javafx.busy;

/**
 * Is called when a system command has been finished
 */
public interface FinishedCallback {
  FinishedCallback NONE = new FinishedCallback() {
    @Override
    public void finished() {
    }

    @Override
    public void timedOut() {
    }
  };

  /**
   * Is called when a task has been finished
   */
  void finished();

  /**
   * Is called when a task has timed out
   */
  void timedOut();
}
