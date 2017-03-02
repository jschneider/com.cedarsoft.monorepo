package com.cedarsoft.test.utils;

import com.google.common.base.Joiner;
import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ThreadRule implements TestRule {

  public static final String STACK_TRACE_ELEMENT_SEPARATOR = "\n\tat ";

  @Nullable
  private final ThreadMatcher ignoredThreadMatcher;

  public ThreadRule() {
    this( new DefaultThreadMatcher() );
  }

  public ThreadRule( @Nullable ThreadMatcher ignoredThreadMatcher ) {
    this.ignoredThreadMatcher = ignoredThreadMatcher;
  }

  @Override
  public Statement apply( final Statement base, Description description ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        try {
          before();
          try {
            base.evaluate();
          } catch (Throwable t) {
            afterFailing();
            throw t;
          }
          after();
        } catch (Throwable throwable) {
          throw new AssertionError("Thread rule failed with <" + throwable.getMessage() + "> " + Joiner.on("\n").join(throwable.getStackTrace()), throwable);
        }
      }
    };
  }

  private Collection<Thread> initialThreads;

  private void before() {
    if ( initialThreads != null ) {
      System.out.println("--> " + "???");
      throw new IllegalStateException( "???" );
    }

    initialThreads = Thread.getAllStackTraces().keySet();
  }

  @Nonnull
  public Collection<? extends Thread> getInitialThreads() {
    if ( initialThreads == null ) {
      System.out.println("not initialized yet");
      throw new IllegalStateException( "not initialized yet" );
    }
    return Collections.unmodifiableCollection( initialThreads );
  }

  private void afterFailing() {
    Set<? extends Thread> remainingThreads = getRemainingThreads();
    if ( !remainingThreads.isEmpty() ) {
      System.err.print( "Some threads have been left:\n" + buildMessage( remainingThreads ) );
    }
  }

  private void after() {
    Set<? extends Thread> remainingThreads = getRemainingThreads();
    if ( !remainingThreads.isEmpty() ) {
      System.out.println("--> " + "Some threads have been left:\n" + buildMessage(remainingThreads));
      throw new IllegalStateException( "Some threads have been left:\n" + buildMessage( remainingThreads ) );
    }
  }

  @Nonnull
  public Set<? extends Thread> getRemainingThreads() {
    Collection<Thread> threadsNow = Thread.getAllStackTraces().keySet();

    Set<Thread> remainingThreads = new HashSet<Thread>( threadsNow );
    remainingThreads.removeAll( initialThreads );

    for ( Iterator<Thread> iterator = remainingThreads.iterator(); iterator.hasNext(); ) {
      Thread remainingThread = iterator.next();
      if ( !remainingThread.isAlive() ) {
        iterator.remove();
        continue;
      }

      //Ignore the threads
      if ( this.ignoredThreadMatcher != null && ignoredThreadMatcher.shallIgnore( remainingThread ) ) {
        iterator.remove();
        continue;
      }

      //Wait for a little bit, sometimes the threads die off
      for (int i = 0; i < 10; i++) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException ignore) {
        }

        //Second try
        if (!remainingThread.isAlive()) {
          iterator.remove();
          break;
        }
      }
    }
    return remainingThreads;
  }

  @Nonnull
  private String buildMessage( @Nonnull Set<? extends Thread> remainingThreads ) {
    StringBuilder builder = new StringBuilder();

    builder.append( "// Remaining Threads:" ).append( "\n" );
    builder.append( "-----------------------" ).append( "\n" );
    for ( Thread remainingThread : remainingThreads ) {
      builder.append( "---" );
      builder.append( "\n" );
      builder.append( remainingThread );
      builder.append( STACK_TRACE_ELEMENT_SEPARATOR );
      builder.append( Joiner.on( STACK_TRACE_ELEMENT_SEPARATOR ).join( remainingThread.getStackTrace() ) );
      builder.append( "\n" );
    }
    builder.append( "-----------------------" ).append( "\n" );

    return builder.toString();
  }

  public interface ThreadMatcher {
    boolean shallIgnore( @Nonnull Thread remainingThread );
  }

  /**
   * Default implementation that ignore several known threads.
   */
  public static class DefaultThreadMatcher implements ThreadMatcher {
    @Override
    public boolean shallIgnore( @Nonnull Thread remainingThread ) {
      @Nullable ThreadGroup threadGroup = remainingThread.getThreadGroup();
      if ( threadGroup == null ) {
        //this means the thread has died
        return true;
      }
      String threadGroupName = threadGroup.getName();
      String threadName = remainingThread.getName();

      if (threadGroupName.equals("system") &&
        threadName.equals( "Keep-Alive-Timer" )
        ||
        threadGroupName.equals( "system" ) &&
          threadName.equals( "process reaper" )
        ||
        threadGroupName.equals( "system" ) &&
          threadName.equals( "Keep-Alive-SocketCleaner" )
        ||
        threadGroupName.equals( "system" ) &&
          threadName.equals( "Java2D Disposer" )
        ||
        threadGroupName.equals( "system" ) &&
          threadName.equals( "AWT-XAWT" )
        ||
        threadGroupName.equals( "main" ) &&
          threadName.equals( "AWT-Shutdown" )
        ||
        threadGroupName.equals( "main" ) &&
          threadName.equals( "AWT-EventQueue-0" )
        ||
        threadGroupName.equals( "main" ) &&
          threadName.equals( "AWT-Windows" )
        ||
        threadGroupName.equals( "main" ) &&
          threadName.startsWith( "QuantumRenderer" )
        ) {
        return true;
      }

      //Special check for awaitility - this lib leaves one thread open for about 100ms
      for (StackTraceElement stackTraceElement : remainingThread.getStackTrace()) {
        if (stackTraceElement.getClassName().equals("org.awaitility.core.ConditionAwaiter$1")) {
          if (stackTraceElement.getMethodName().equals("run")) {
            return true;
          }
        }
      }

      return false;
    }
  }
}
