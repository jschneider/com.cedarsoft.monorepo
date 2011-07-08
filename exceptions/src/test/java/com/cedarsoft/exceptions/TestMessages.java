package com.cedarsoft.exceptions;

import javax.annotation.Nonnull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
* @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
*/
public enum TestMessages implements ApplicationException.Message {
  ERROR_1,
  ERROR_2;


  @Override
  @Nonnull
  public String getKey() {
    return getClass().getName() + "." + name();
  }

  @Nonnull
  @Override
  public String getLocalizedMessage( @Nonnull Object... messageArguments ) {
    return getLocalizedMessage( Locale.getDefault(), messageArguments );
  }

  @Nonnull
  @Override
  public String getLocalizedMessage( @Nonnull Locale locale, @Nonnull Object... messageArguments ) {
    String bundleValue = getBundleValue( locale );
    return MessageFormat.format( bundleValue, messageArguments );
  }

  @Nonnull
  public String getBundleValue( @Nonnull Locale locale ) {
    return ResourceBundle.getBundle( ApplicationExceptionTest.BUNDLE, locale ).getString( getKey() );
  }
}
