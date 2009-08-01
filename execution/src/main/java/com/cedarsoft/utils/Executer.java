package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes a process and notifies execution listeners whenever the process has finished
 */
@SuppressWarnings( {"UseOfProcessBuilder"} )
public class Executer {
  @NotNull
  private final List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();

  @NotNull
  private final ProcessBuilder processBuilder;
  private boolean redirectStreams = true;

  public Executer( @NotNull ProcessBuilder processBuilder, boolean redirectStreams ) {
    this.processBuilder = processBuilder;
    this.redirectStreams = redirectStreams;
  }

  public Executer( @NotNull ProcessBuilder processBuilder ) {
    this( processBuilder, true );
  }

  public int execute() throws IOException, InterruptedException {
    Process process = processBuilder.start();
    redirectStreams( process );
    notifyExecutionStarted( process );

    int answer = process.waitFor();
    notifyExecutionFinished( answer );
    return answer;
  }

  protected void redirectStreams( @NotNull Process process ) {
    if ( redirectStreams ) {
      OutputRedirector.redirect( process );
    }
  }

  public void executeAsync() {
    new Thread( new Runnable() {
      public void run() {
        try {
          execute();
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    } ).start();
  }

  private void notifyExecutionFinished( int answer ) {
    for ( ExecutionListener executionListener : executionListeners ) {
      executionListener.executionFinished( answer );
    }
  }

  private void notifyExecutionStarted( @NotNull Process process ) {
    for ( ExecutionListener executionListener : executionListeners ) {
      executionListener.executionStarted( process );
    }
  }

  public void addExecutionListener( @NotNull ExecutionListener executionListener ) {
    this.executionListeners.add( executionListener );
  }

  public void removeExecutionListener( @NotNull ExecutionListener executionListener ) {
    this.executionListeners.remove( executionListener );
  }

  public boolean isRedirectStreams() {
    return redirectStreams;
  }

  public void setRedirectStreams( boolean redirectStreams ) {
    this.redirectStreams = redirectStreams;
  }
}
