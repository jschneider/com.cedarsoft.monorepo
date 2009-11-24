package com.cedarsoft.exec;

import org.jetbrains.annotations.NotNull;

/**
 * Listener that is notified about the execution of a process
 */
public interface ExecutionListener {

  /**
   * Is notified whenever the process has been started
   *
   * @param process the process that has been started
   */
  void executionStarted( @NotNull Process process );

  /**
   * Is called whenever the execution has been finished
   *
   * @param answer the answer
   */
  void executionFinished( int answer );
}
