package com.cedarsoft.async;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 */
public class AsyncCallSupportTest {
  private AsyncCallSupport<MyAction> callSupport;
  private Thread testThread;
  private MyAction action;
  private static final int THREAD_COUNT = 1000;

  @BeforeMethod
  protected void setUp() throws Exception {
    callSupport = new AsyncCallSupport<MyAction>();
    callSupport.initializeWorker( new CallbackCaller<MyAction>() {
      @java.lang.Override
      public Object call( @NotNull MyAction callback ) throws Exception {
        ensureNotThisThread();
        return callback.count();
      }

      @java.lang.Override
      @NotNull
      public String getDescription() {
        return "MyCaller";
      }
    } );
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
  public void testBasic() {
    assertEquals( 0, callSupport.invoke( action ) );
    assertEquals( 1, callSupport.invokeNullable( action ) );
    assertEquals( 2, callSupport.invoke( action ) );
    callSupport.invokeVoid( action );
    assertEquals( 4, callSupport.invoke( action ) );

    try {
      callSupport.invokeVoid( action );
      fail( "Where is the Exception" );
    } catch ( AsynchroniousInvocationException e ) {
      assertEquals( e.getCause().getClass(), IllegalStateException.class );
    }
    assertEquals( 6, callSupport.invoke( action ) );
  }

  @Test
  public void testMultipleThreads() throws InterruptedException {
    new Thread( new Runnable() {
      @java.lang.Override
      public void run() {
        assertEquals( 0, callSupport.invoke( action ) );
      }
    } ).start();

    Thread.yield();
    Thread.sleep( 100 );
    assertEquals( 1, callSupport.invoke( action ) );

    new Thread( new Runnable() {
      @java.lang.Override
      public void run() {
        try {
          Thread.sleep( 1000 );
        } catch ( InterruptedException ignore ) {
        }
        assertEquals( 4, callSupport.invoke( action ) );
      }
    } ).start();

    assertEquals( 2, callSupport.invoke( action ) );
    assertEquals( 3, callSupport.invoke( action ) );

    Thread.sleep( 1050 );

    try {
      callSupport.invoke( action );
      fail( "Where is the Exception" );
    } catch ( AsynchroniousInvocationException e ) {
    }

    assertEquals( 6, callSupport.invoke( action ) );
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
        @java.lang.Override
        public void run() {
          try {
            Thread.sleep( random.nextInt( 100 ) );
          } catch ( InterruptedException ignore ) {
          }
          try {
            callSupport.invoke( new MyAction() );
          } finally {
            synchronized ( finished ) {
              finished.add( this );
              finished.notifyAll();
            }
          }
        }
      };
      new Thread( runnable ).start();
      started.add( runnable );
    }

    assertEquals( THREAD_COUNT, started.size() );

    synchronized ( finished ) {
      while ( finished.size() < 100 ) {//one has an exception
        System.out.println( "waiting " + finished.size() );
        finished.wait();
      }
    }

    //todo verify
  }

  public static class MyAction {
    private int counter;

    public int count() {
      if ( counter == 5 ) {
        counter++;
        throw new IllegalStateException( "reached 5" );
      }
      return counter++;
    }
  }
}
