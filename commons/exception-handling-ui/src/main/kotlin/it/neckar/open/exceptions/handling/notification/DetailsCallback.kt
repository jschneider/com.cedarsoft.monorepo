package it.neckar.open.exceptions.handling.notification

import it.neckar.open.annotations.UiThread

/**
 * Callback that is notified when the user clicks the details link/button
 */
@FunctionalInterface
interface DetailsCallback {
  @UiThread
  fun detailsClicked(notification: Notification)
}
