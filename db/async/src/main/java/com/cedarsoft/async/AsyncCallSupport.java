package com.cedarsoft.async;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * An asynchronous call.
 *
 * @param <T> the type of calls
 */
public class AsyncCallSupport<T> {
  @NonNls
  @NotNull
  private static final Log log = LogFactory.getLog( AsyncCallSupport.class );

  @NotNull
  @NonNls
  public static final String NAME_PREFIX = "AsyncWorkerThread ";

  /**
   * The call queue contains all calls
   */
  @NotNull
  private final Queue<T> callbacksQueue = new LinkedList<T>();
  @NotNull
  private final Map<T, Object> returnValues = new HashMap<T, Object>();
  @NotNull
  private final Set<T> acks = new HashSet<T>();

  private boolean initialized;


  //The worker thread that might have been started
  private transient Thread workerThread;

  public void initializeWorker( @NotNull CallbackCaller<T> callbackCaller ) {
    log.debug( "initializeWorker " + callbackCaller );
    workerThread = new Thread( new AsyncWorker<T>( this, callbackCaller ), NAME_PREFIX + callbackCaller.getDescription() + ' ' + hashCode() );
    workerThread.start();
    initialized = true;
  }

  private void initialize() {//todo remove
  }

  public void shutdown() {
    workerThread.interrupt();
  }

  /**
   * Invokes a callback
   *
   * @param callback the callback
   * @return the return value of the callback
   */
  @NotNull
  public <R> R invoke( T callback ) throws AsynchroniousInvocationException {
    R value = this.<R>invokeNullable( callback );
    if ( value == null ) {
      throw new IllegalStateException( "Return values was null. Expected not null." );
    }
    return value;
  }

  /**
   * Invoke a nullable callback
   *
   * @param callback the callback
   * @return the return value (may be null)
   */
  @Nullable
  public <R> R invokeNullable( @NotNull T callback ) throws AsynchroniousInvocationException {
    long start = System.currentTimeMillis();
    log.debug( "invoking Nullable " + callback );
    log.debug( "Is initialized: " + initialized );
    if ( !isInitialized() ) {
      initialize();
    }

    synchronized ( callbacksQueue ) {
      log.debug( "got Lock on callbacksQueue" );
      callbacksQueue.add( callback );
      log.debug( "added callback" );
      callbacksQueue.notifyAll();
      log.debug( "notified callbacks queue (added callback with hash code " + callback.hashCode() + ')' );
    }

    synchronized ( acks ) {
      log.debug( "got log on acks" );
      while ( !acks.remove( callback ) ) {
        try {
          log.debug( "waiting for acks - nothing found. " + acks.size() );
          acks.wait();
        } catch ( InterruptedException e ) {
          throw new AsynchroniousInvocationException( "Interrupted", e );
        }
      }
    }

    synchronized ( returnValues ) {
      log.debug( "got lock on return values" );

      Object returnValue = returnValues.remove( callback );

      long end = System.currentTimeMillis();
      log.info( "Database action took " + ( end - start ) + " ms - returning <" + returnValue + "> for " + callback );

      /**
       * If it is an exception --> throw it
       */
      if ( returnValue instanceof Throwable ) {
        log.warn( "Wrapping Throwable: " + returnValue );
        throw new AsynchroniousInvocationException( ( Throwable ) returnValue );
      }

      //No exception, simple return value
      log.debug( "Returning " + returnValue );
      //noinspection unchecked
      return ( R ) returnValue;
    }
  }

  /**
   * Invokes a void callback
   *
   * @param callback the callback
   */
  public void invokeVoid( @NotNull T callback ) throws AsynchroniousInvocationException {
    invokeNullable( callback );
  }

  public boolean isInitialized() {
    return initialized;
  }

  /**
   * Returns the next action
   *
   * @return the next action from the queue
   */
  @NotNull
  protected T waitForNextCallback() throws InterruptedException {
    T callback;
    synchronized ( callbacksQueue ) {
      log.debug( "got lock on callbacks queue" );
      while ( callbacksQueue.isEmpty() ) {
        log.debug( "nothing in callbacksqueue - waiting" );
        callbacksQueue.wait();
      }
      callback = callbacksQueue.poll();
    }
    log.debug( "new callback added - waiting successfull. Returning " + callback );
    return callback;
  }

  /**
   * Acknowledges the asynchronous dao that an action has been executed
   *
   * @param action      the action that has been executed
   * @param returnValue the return value (or exception)
   */
  protected void acknowledge( @NotNull T action, @Nullable Object returnValue ) {
    log.debug( "acknowleding " + action.hashCode() + " with return value " + returnValue );
    synchronized ( returnValues ) {
      returnValues.put( action, returnValue );
    }
    log.debug( "Added return value" );
    synchronized ( acks ) {
      log.debug( "Got Lock on acks" );
      int oldSize = acks.size();
      acks.add( action );
      if ( oldSize == acks.size() ) {
        throw new IllegalStateException( "You must not reuse the action " + action + " with return value " + returnValue );
      }
      acks.notifyAll();
      log.debug( "notified acks" );
    }
  }

  /**
   * Verifies that no worker threads have been left
   *
   * @throws InterruptedException
   */
  public static void verifyNoWorkerThreadsLeft() throws InterruptedException {
    Thread.sleep( 1000 );

    ThreadGroup group = Thread.currentThread().getThreadGroup();
    Thread[] threads = new Thread[group.activeCount()];
    group.enumerate( threads );

    StringBuilder found = new StringBuilder();

    for ( Thread thread : threads ) {
      if ( thread.getName().contains( NAME_PREFIX ) ) {
        System.out.println( "---> Uups, found one! " + thread );
        found.append( "Found remaing thread " + thread + "\n" );
      }
    }

    if ( found.length() > 0 ) {
      throw new IllegalThreadStateException( found.toString() );
    }
  }

  public static int countWorkerThreadsLeft() throws InterruptedException {
    Thread.sleep( 1000 );

    ThreadGroup group = Thread.currentThread().getThreadGroup();
    Thread[] threads = new Thread[group.activeCount()];
    group.enumerate( threads );

    int count = 0;

    StringBuilder found = new StringBuilder();

    for ( Thread thread : threads ) {
      if ( thread.getName().contains( NAME_PREFIX ) ) {
        count++;
      }
    }

    return count;
  }


  public static void shutdownWorkerThreads() throws InterruptedException {
    ThreadGroup group = Thread.currentThread().getThreadGroup();
    Thread[] threads = new Thread[group.activeCount()];
    group.enumerate( threads );

    for ( Thread thread : threads ) {
      if ( thread.getName().contains( NAME_PREFIX ) ) {
        thread.interrupt();
        thread.stop();
      }
    }

    verifyNoWorkerThreadsLeft();
  }

  static class AsyncWorker<T> implements Runnable {
    private static final Log log = LogFactory.getLog( AsyncCallSupport.AsyncWorker.class );

    @NotNull
    private final AsyncCallSupport<T> support;
    @NotNull
    private final CallbackCaller<T> callbackCaller;

    AsyncWorker( @NotNull AsyncCallSupport<T> support, @NotNull CallbackCaller<T> callbackCaller ) {
      this.support = support;
      this.callbackCaller = callbackCaller;
    }

    public void run() {
      log.debug( "up and running" );
      //noinspection InfiniteLoopStatement
      while ( true ) {
        if ( Thread.currentThread().isInterrupted() ) {
          log.debug( "Thread has been interrupted --> exiting" );
          return;
        }

        try {
          log.debug( "waiting for next callback" );
          T callback = support.waitForNextCallback();
          log.debug( "got callback" );

          Object returnValue;
          synchronized ( support ) {
            returnValue = execute( callback );
          }
          log.debug( "executed" );
          support.acknowledge( callback, returnValue );
          log.debug( "acknowledged" );
        } catch ( InterruptedException ignore ) {
          log.debug( "Thread has been interrupted --> exiting" );
          return;
        }
      }
    }

    @Nullable
    protected Object execute( @NotNull T callback ) {
      long start = System.currentTimeMillis();
      Object returnValue;
      //noinspection CatchGenericClass
      try {
        returnValue = callbackCaller.call( callback );
      } catch ( Throwable e ) {
        log.warn( "Got an exception", e );
        returnValue = e;
      }

      long end = System.currentTimeMillis();
      log.info( "Executing callback took " + ( end - start ) + "ms" );

      return returnValue;
    }
  }
}
