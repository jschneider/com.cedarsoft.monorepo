package com.cedarsoft.exceptions.handling.notification;

import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;

/**
 * Callback that is notified when the user clicks the details link/button
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@FunctionalInterface
public interface DetailsCallback {
  @UiThread
  void detailsClicked(@Nonnull Notification notification);
}
