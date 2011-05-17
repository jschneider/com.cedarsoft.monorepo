package com.cedarsoft;


import javax.annotation.Nonnull;

import java.util.Locale;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ApplicationException extends RuntimeException {
  @Nonnull
  private final Message message;
  @Nonnull
  private final Object[] messageArguments;

  public ApplicationException( @Nonnull Message message, @Nonnull Object... messageArguments ) {
    super( message.getKey() );
    this.message = message;
    this.messageArguments = messageArguments.clone();
  }

  @Nonnull
  public Object[] getMessageArguments() {
    return messageArguments.clone();
  }

  /**
   * @noinspection RefusedBequest
   */
  @Override
  @Nonnull
  public String getLocalizedMessage() {
    return message.getLocalizedMessage( messageArguments );
  }

  @Nonnull
  public String getLocalizedMessage( @Nonnull Locale locale ) {
    return message.getLocalizedMessage( locale, messageArguments );
  }

  public interface Message {
    @Nonnull
    String getKey();

    @Nonnull
    String getLocalizedMessage( @Nonnull Object... messageArguments );

    @Nonnull
    String getLocalizedMessage( @Nonnull Locale locale, @Nonnull Object... messageArguments );
  }
}
