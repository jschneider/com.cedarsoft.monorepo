package com.cedarsoft.exceptions;

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
