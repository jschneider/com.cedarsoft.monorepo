package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

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
  @NotNull
  public String getKey() {
    return getClass().getName() + "." + name();
  }

  @NotNull
  @Override
  public String getLocalizedMessage( @NotNull Object... messageArguments ) {
    return getLocalizedMessage( Locale.getDefault(), messageArguments );
  }

  @NotNull
  @Override
  public String getLocalizedMessage( @NotNull Locale locale, @NotNull Object... messageArguments ) {
    String bundleValue = getBundleValue( locale );
    return MessageFormat.format( bundleValue, messageArguments );
  }

  @NotNull
  public String getBundleValue( @NotNull Locale locale ) {
    return ResourceBundle.getBundle( ApplicationExceptionTest.BUNDLE, locale ).getString( getKey() );
  }
}
