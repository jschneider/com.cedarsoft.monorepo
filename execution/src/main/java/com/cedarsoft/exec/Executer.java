/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
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
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
@SuppressWarnings( {"UseOfProcessBuilder"} )
public class Executer {
  @NotNull
  private final List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();

  @NotNull
  private final ProcessBuilder processBuilder;
  private boolean redirectStreams = true;

  /**
   * <p>Constructor for Executer.</p>
   *
   * @param processBuilder a {@link java.lang.ProcessBuilder} object.
   * @param redirectStreams a boolean.
   */
  public Executer( @NotNull ProcessBuilder processBuilder, boolean redirectStreams ) {
    this.processBuilder = processBuilder;
    this.redirectStreams = redirectStreams;
  }

  /**
   * <p>Constructor for Executer.</p>
   *
   * @param processBuilder a {@link java.lang.ProcessBuilder} object.
   */
  public Executer( @NotNull ProcessBuilder processBuilder ) {
    this( processBuilder, true );
  }

  /**
   * <p>execute</p>
   *
   * @return a int.
   * @throws java.io.IOException if any.
   * @throws java.lang.InterruptedException if any.
   */
  public int execute() throws IOException, InterruptedException {
    Process process = processBuilder.start();
    redirectStreams( process );
    notifyExecutionStarted( process );

    int answer = process.waitFor();
    notifyExecutionFinished( answer );
    return answer;
  }

  /**
   * <p>redirectStreams</p>
   *
   * @param process a {@link java.lang.Process} object.
   */
  protected void redirectStreams( @NotNull Process process ) {
    if ( redirectStreams ) {
      OutputRedirector.redirect( process );
    }
  }

  /**
   * <p>executeAsync</p>
   */
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

  /**
   * <p>addExecutionListener</p>
   *
   * @param executionListener a {@link com.cedarsoft.exec.ExecutionListener} object.
   */
  public void addExecutionListener( @NotNull ExecutionListener executionListener ) {
    this.executionListeners.add( executionListener );
  }

  /**
   * <p>removeExecutionListener</p>
   *
   * @param executionListener a {@link com.cedarsoft.exec.ExecutionListener} object.
   */
  public void removeExecutionListener( @NotNull ExecutionListener executionListener ) {
    this.executionListeners.remove( executionListener );
  }

  /**
   * <p>isRedirectStreams</p>
   *
   * @return a boolean.
   */
  public boolean isRedirectStreams() {
    return redirectStreams;
  }

  /**
   * <p>Setter for the field <code>redirectStreams</code>.</p>
   *
   * @param redirectStreams a boolean.
   */
  public void setRedirectStreams( boolean redirectStreams ) {
    this.redirectStreams = redirectStreams;
  }
}
