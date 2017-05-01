package com.cedarsoft.exceptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Non-fatal exception. Is shown to the user as notification balloon.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NotificationException extends RuntimeException {
  @Nonnull
  private final String title;
  @Nonnull
  private final String message;

  public NotificationException(@Nonnull String title, @Nonnull String message) {
    this(null, title, message);
  }

  public NotificationException(@Nullable Throwable cause, @Nonnull String title, @Nonnull String message) {
    super(cause);
    this.title = title;
    this.message = message;
  }

  @Nonnull
  public String getTitle() {
    return title;
  }

  @Override
  @Nonnull
  public String getMessage() {
    return message;
  }
}
