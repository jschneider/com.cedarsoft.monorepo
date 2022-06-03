package com.cedarsoft.exceptions.handling.notification

import com.cedarsoft.annotations.UiThread

/**
 * Callback that is notified when the user clicks the details link/button
 */
@FunctionalInterface
interface DetailsCallback {
  @UiThread
  fun detailsClicked(notification: Notification)
}
