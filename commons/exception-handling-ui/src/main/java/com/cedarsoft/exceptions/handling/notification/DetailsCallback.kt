package com.cedarsoft.exceptions.handling.notification

import com.cedarsoft.annotations.UiThread

/**
 * Callback that is notified when the user clicks the details link/button
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@FunctionalInterface
interface DetailsCallback {
  @UiThread
  fun detailsClicked(notification: Notification)
}
