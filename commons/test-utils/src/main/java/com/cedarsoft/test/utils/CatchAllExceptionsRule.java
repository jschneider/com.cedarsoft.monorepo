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
package com.cedarsoft.test.utils;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This rule catches exceptions on all threads and fails the test if such exceptions are caught
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CatchAllExceptionsRule implements TestRule {
  @Nullable
  private Thread.UncaughtExceptionHandler oldHandler;

  @Override
  public Statement apply( final Statement base, Description description ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        before();
        try {
          base.evaluate();
        } catch ( Throwable t ) {
          afterFailing();
          throw t;
        }

        afterSuccess();
      }
    };
  }

  private void before() {
    oldHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler() {
      @Override
      public void uncaughtException( Thread t, Throwable e ) {
        caught.add( e );
        if ( oldHandler != null ) {
          oldHandler.uncaughtException( t, e );
        }
      }
    } );
  }

  @Nonnull
  private final List<Throwable> caught = new ArrayList<Throwable>();

  private void afterSuccess() {
    Thread.setDefaultUncaughtExceptionHandler( oldHandler );

    if ( caught.isEmpty() ) {
      return;
    }

    throw new AssertionError( buildMessage() );
  }

  private String buildMessage() {
    StringBuilder builder = new StringBuilder();
    builder.append( caught.size() ).append( " exceptions thrown but not caught in other threads:\n" );

    for ( Throwable throwable : caught ) {
      builder.append( "---------------------\n" );

      StringWriter out = new StringWriter();
      throwable.printStackTrace( new PrintWriter( out ) );
      builder.append( out.toString() );
    }

    builder.append( "---------------------\n" );

    return builder.toString();
  }

  private void afterFailing() {
    Thread.setDefaultUncaughtExceptionHandler( oldHandler );
  }
}
