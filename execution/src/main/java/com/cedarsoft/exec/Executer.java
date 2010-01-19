/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.exec;

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
      @Override
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
