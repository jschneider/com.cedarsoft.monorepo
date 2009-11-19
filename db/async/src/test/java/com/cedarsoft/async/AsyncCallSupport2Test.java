package com.cedarsoft.async;

import org.jetbrains.annotations.NotNull;
import org.testng.*;
import org.testng.annotations.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import static org.testng.Assert.*;

/**
 *
 */
public class AsyncCallSupport2Test {
  private AsyncCallSupport2<Integer> callSupport;
  private Thread testThread;
  private MyAction action;
  private static final int THREAD_COUNT = 1000;

  @BeforeMethod
  protected void setUp() throws Exception {
    callSupport = new AsyncCallSupport2<Integer>();

    testThread = Thread.currentThread();
    action = new MyAction();
  }

  @AfterMethod
  protected void tearDown() throws Exception {
    callSupport.shutdown();
    AsyncCallSupport.verifyNoWorkerThreadsLeft();
  }

  @Test
  public void testEmpty() {

  }

  private void ensureNotThisThread() {
    assertNotSame( testThread, Thread.currentThread() );
  }

  @Test
  public void testBasic() throws ExecutionException, InterruptedException {
    Assert.assertEquals( 0, callSupport.invoke( action ).get().intValue() );
    assertEquals( 1, callSupport.invoke( action ).get().intValue() );
    assertEquals( 2, callSupport.invoke( action ).get().intValue() );
    callSupport.invoke( action ).get();
    assertEquals( 4, callSupport.invoke( action ).get().intValue() );

    try {
      callSupport.invoke( action ).get();
      fail( "Where is the Exception" );
    } catch ( ExecutionException e ) {
      assertEquals( e.getCause().getClass(), IllegalStateException.class );
      assertEquals( "reached 5", e.getCause().getMessage() );
    }
    assertEquals( 6, callSupport.invoke( action ).get().intValue() );
  }

  @Test
  public void testMultipleThreads() throws InterruptedException, ExecutionException {
    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          assertEquals( 0, callSupport.invoke( action ).get().intValue() );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    } ).start();

    Thread.sleep( 50 );
    assertEquals( 1, callSupport.invoke( action ).get().intValue() );

    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep( 1000 );
        } catch ( InterruptedException ignore ) {
        }
        try {
          assertEquals( 4, callSupport.invoke( action ).get().intValue() );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    } ).start();

    assertEquals( 2, callSupport.invoke( action ).get().intValue() );
    assertEquals( 3, callSupport.invoke( action ).get().intValue() );

    Thread.sleep( 1050 );

    try {
      callSupport.invoke( action ).get();
      fail( "Where is the Exception" );
    } catch ( ExecutionException e ) {
    }

    assertEquals( 6, callSupport.invoke( action ).get().intValue() );
  }

  private Random random = new Random();
  private boolean missed5 = false;
  private final Object missedMonitor = new Object();

  @Test
  public void testMassiveThreads() throws InterruptedException {
    final Set<Runnable> finished = Collections.synchronizedSet( new HashSet<Runnable>() );
    final Set<Runnable> started = Collections.synchronizedSet( new HashSet<Runnable>() );

    for ( int i = 0; i < THREAD_COUNT; i++ ) {
      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep( random.nextInt( 100 ) );
          } catch ( InterruptedException ignore ) {
          }
          try {
            callSupport.invoke( new MyAction() ).get();
            assertFalse( callSupport.isShutdown() );
          } catch ( RejectedExecutionException e ) {
            System.out.println( "---> UUPS" );
            throw e;
          } catch ( Exception e ) {
            throw new RuntimeException( e );
          }

          synchronized ( finished ) {
            finished.add( this );
            finished.notifyAll();
          }
        }
      };
      new Thread( runnable ).start();
      started.add( runnable );
    }

    assertEquals( THREAD_COUNT, started.size() );

    synchronized ( finished ) {
      while ( finished.size() < THREAD_COUNT ) {//one has an exception
        finished.wait();
      }
    }

    System.out.println( "finished..." );
  }

  public static class MyAction implements Callable<Integer> {
    private int counter;

    @Override
    @NotNull
    public Integer call() throws Exception {
      if ( counter == 5 ) {
        counter++;
        throw new IllegalStateException( "reached 5" );
      }
      return counter++;
    }
  }
}
