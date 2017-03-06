package com.cedarsoft.exceptions.handling.notification;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification that can be shown in a balloon
 */
public class Notification {
  @Nonnull
  private final String title;
  @Nonnull
  private final String message;

  @Nullable
  private final NotificationService.DetailsCallback detailsCallback;

  public Notification(@Nonnull String title, @Nonnull String message, @Nullable NotificationService.DetailsCallback detailsCallback) {
    this.title = title;
    this.message = message;
    this.detailsCallback = detailsCallback;
  }

  @Nonnull
  public String getMessage() {
    return message;
  }

  @Nonnull
  public String getTitle() {
    return title;
  }

  @Nullable
  public NotificationService.DetailsCallback getDetailsCallback() {
    return detailsCallback;
  }
}
