package com.cedarsoft.exceptions;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Locale;

/**
* @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
*/
public enum TestExceptionDetails implements ApplicationException.Details {
  ERROR_1( 701 ),
  ERROR_2( 702 );

  @Nonnull
  private final ErrorCode errorCode;

  TestExceptionDetails( int errorCode ) {
    this( new ErrorCode( TestException.PREFIX, errorCode ) );
  }

  TestExceptionDetails( @Nonnull ErrorCode errorCode ) {
    this.errorCode = errorCode;
  }

  @Nonnull
  @Override
  public String getLocalizedMessage( @Nonnull Object... messageArguments ) {
    return MessageFormat.format( TestMessages.get( this ), messageArguments );
  }

  @Nonnull
  @Override
  public String getLocalizedMessage( @Nonnull Locale locale, @Nonnull Object... messageArguments ) {
    return MessageFormat.format( TestMessages.get( this ), messageArguments ); //todo locale!
  }

  @Nonnull
  @Override
  public String getTitle( @Nonnull Object... messageArguments ) {
    return MessageFormat.format( TestMessages.get( this, "title" ), messageArguments );
  }

  @Nonnull
  @Override
  public String getTitle( @Nonnull Locale locale, @Nonnull Object... messageArguments ) {
    return MessageFormat.format( TestMessages.get( this, "title" ), messageArguments ); //todo locale
  }

  @Nonnull
  @Override
  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
