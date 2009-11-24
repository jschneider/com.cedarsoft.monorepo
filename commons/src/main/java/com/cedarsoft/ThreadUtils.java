package com.cedarsoft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Offers static thread utils
 */
public class ThreadUtils {
  public static boolean isEventDispatchThread() {
    return SwingUtilities.isEventDispatchThread();
  }

  public static void assertEventDispatchThread() throws IllegalThreadStateException {
    if ( !isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "Not in EDT" );
    }
  }

  public static void assertNotEventDispatchThread() throws IllegalThreadStateException {
    if ( isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "Is EDT" );
    }
  }

  @Nullable
  public static <T> T inokeInOtherThread( @NotNull Callable<T> callable ) throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      Future<T> future = executor.submit( callable );
      return future.get();
    } finally {
      executor.shutdown();
    }
  }

  /**
   * Invokes the runnable within the EDT
   */
  public static void invokeInEventDispatchThread( @NotNull Runnable runnable ) {
    if ( isEventDispatchThread() ) {
      runnable.run();
    } else {
      try {
        SwingUtilities.invokeAndWait( runnable );
      } catch ( InterruptedException e ) {
        throw new RuntimeException( e );
      } catch ( InvocationTargetException e ) {
        throw new RuntimeException( e );
      }
    }
  }
}
