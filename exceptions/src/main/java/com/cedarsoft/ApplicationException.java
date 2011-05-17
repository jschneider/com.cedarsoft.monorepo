package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ApplicationException extends RuntimeException {
  @NotNull
  private final Message message;
  @NotNull
  private final Object[] messageArguments;

  public ApplicationException( @NotNull Message message, @NotNull Object... messageArguments ) {
    super( message.getKey() );
    this.message = message;
    this.messageArguments = messageArguments.clone();
  }

  @NotNull
  public Object[] getMessageArguments() {
    return messageArguments.clone();
  }

  /**
   * @noinspection RefusedBequest
   */
  @Override
  @NotNull
  @NonNls
  public String getLocalizedMessage() {
    return message.getLocalizedMessage( messageArguments );
  }

  @NotNull
  @NonNls
  public String getLocalizedMessage( @NotNull Locale locale ) {
    return message.getLocalizedMessage( locale, messageArguments );
  }

  public interface Message {
    @NotNull
    @NonNls
    String getKey();

    @NotNull
    String getLocalizedMessage( @NotNull Object... messageArguments );

    @NotNull
    String getLocalizedMessage( @NotNull Locale locale, @NotNull Object... messageArguments );
  }
}
