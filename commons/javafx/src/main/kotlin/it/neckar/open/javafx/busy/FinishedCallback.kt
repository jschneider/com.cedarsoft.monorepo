package it.neckar.open.javafx.busy

/**
 * Is called when a system command has been finished
 */
interface FinishedCallback {
  /**
   * Is called when a task has been finished
   */
  fun finished()

  /**
   * Is called when a task has timed out
   */
  fun timedOut()

  companion object {
    @JvmStatic
    val None: FinishedCallback = object : FinishedCallback {
      override fun finished() {}
      override fun timedOut() {}
    }
  }
}
