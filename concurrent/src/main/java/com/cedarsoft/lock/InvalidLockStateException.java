package com.cedarsoft.lock;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class InvalidLockStateException extends RuntimeException {
  @NotNull
  private final List<String> readLockingThreads = new ArrayList<String>();

  public InvalidLockStateException( @NotNull List<? extends Thread> readLockingThreads ) {
    super( createMessage( readLockingThreads ) );
    for ( Thread readLockingThread : readLockingThreads ) {
      this.readLockingThreads.add( readLockingThread.getName() );
    }
  }

  @NotNull
  public List<String> getReadLockingThreads() {
    return Collections.unmodifiableList( readLockingThreads );
  }

  private static String createMessage( @NotNull List<? extends Thread> readLockingThreads ) {
    StringBuilder message = new StringBuilder().append( "Cannot get write lock because there are still read locks active in: " ).append( "\n" );
    for ( Thread thread : readLockingThreads ) {
      message.append( "\t" ).append( thread.getName() );
    }
    return message.toString();
  }
}
