package com.cedarsoft.lock;

import java.lang.Override;

/**
 *
 */
public class DefaultDeadlockListener implements ThreadDeadlockDetector.Listener {
  @Override
  public void deadlockDetected( Thread[] deadlockedThreads ) {
    System.err.println( "Deadlocked Threads:" );
    System.err.println( "-------------------" );
    for ( Thread thread : deadlockedThreads ) {
      System.err.println( thread );
      for ( StackTraceElement ste : thread.getStackTrace() ) {
        System.err.println( "\t" + ste );
      }
    }
  }
}
