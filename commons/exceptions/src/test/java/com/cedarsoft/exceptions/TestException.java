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
package com.cedarsoft.exceptions;

import javax.annotation.concurrent.Immutable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TestException extends ApplicationException {
  @Nonnull
  public static final ErrorCode.Prefix PREFIX = new ErrorCode.Prefix( "TD" );

  public TestException( @Nonnull TestExceptionDetails exceptionDetails, @Nonnull Object... messageArguments ) {
    super( exceptionDetails, messageArguments );
  }

  /**
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  @Immutable
  public enum TestExceptionDetails implements Details {
    ERROR_1( 701 ),
    ERROR_2( 702 );
    public static final String CATEGORY_TITLE = "title";

    @Nonnull
    private final ErrorCode errorCode;

    @Nonnull
    private final Messages messages = new Messages( "com.cedarsoft.exceptions.testmessages" );

    TestExceptionDetails( int errorCode ) {
      this( new ErrorCode(PREFIX, errorCode ) );
    }

    TestExceptionDetails( @Nonnull ErrorCode errorCode ) {
      this.errorCode = errorCode;
    }

    @Nonnull
    @Override
    public String getLocalizedMessage( @Nullable Object... messageArguments ) {
      return messages.get(this, Locale.getDefault(), messageArguments );
    }

    @Nonnull
    @Override
    public String getLocalizedMessage( @Nonnull Locale locale, @Nullable Object... messageArguments ) {
      return messages.get( this, locale, messageArguments );
    }

    @Nonnull
    @Override
    public String getTitle( @Nullable Object... messageArguments ) {
      return messages.get( this, CATEGORY_TITLE, Locale.getDefault(), messageArguments );
    }

    @Nonnull
    @Override
    public String getTitle( @Nonnull Locale locale, @Nullable Object... messageArguments ) {
      return messages.get( this, CATEGORY_TITLE, locale, messageArguments );
    }

    @Nonnull
    @Override
    public ErrorCode getErrorCode() {
      return errorCode;
    }
  }
}
