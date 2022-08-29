package com.cedarsoft.commons.javafx

import com.cedarsoft.annotations.NonUiThread
import com.cedarsoft.annotations.UiThread
import com.cedarsoft.unit.si.t
import javafx.application.Platform
import javafx.concurrent.Task
import java.util.concurrent.ExecutionException
import javax.annotation.Nonnull

/**
 * Similar to a simple SwingWorker.
 * Must not return "null". Instead, use [java.util.Optional]
 *
 */
abstract class JavaFxWorker<T> : Task<T>() {
  @Throws(Exception::class)
  override fun call(): T? {
    return doInBackground()
  }

  @Throws(Exception::class)
  protected abstract fun doInBackground(): T

  @UiThread
  final override fun succeeded() {
    super.succeeded()
  }

  @UiThread
  final override fun running() {
    super.running()
  }

  @UiThread
  final override fun cancelled() {
    super.cancelled()
  }

  @UiThread
  final override fun failed() {
    super.failed()
  }

  @NonUiThread
  override fun done() {
    super.done()
    Platform.runLater {
      cleanup()
      try {
        success(get())
      } catch (e: InterruptedException) {
        failure(e)
      } catch (e: ExecutionException) {
        failure(e.cause ?: e)
      } catch (e: Exception) {
        failure(e)
      }
    }
  }

  /**
   * Is called before #done(T);
   * This method is called even if [.doInBackground] has thrown an exception
   */
  @UiThread
  protected open fun cleanup() {
  }

  /**
   * Is called the value that is returned by [.doInBackground].
   * Is only called if no exception has been thrown in [.doInBackground].
   */
  @UiThread
  protected open fun success(t: T) {
  }

  @UiThread
  protected open fun failure(e: Throwable) {
    throw e
  }

  /**
   * Schedules for execution
   *
   * @return the thread this worker runs its background stuff on
   */
  @Nonnull
  fun execute(): Thread {
    val thread = Thread(this, "JavaFXWorker-Thread for <" + javaClass.name + ">")
    thread.start()
    return thread
  }

  companion object {
    /**
     * Creates a very simple fx worker
     */
    fun <T> create(backgroundAction: () -> T): JavaFxWorker<T> {
      return object : JavaFxWorker<T>() {
        override fun doInBackground(): T {
          return backgroundAction()
        }
      }
    }

    fun <T> build(configuration: Builder<T>.() -> Unit): Builder<T> {
      return Builder<T>().also {
        it.configuration()
      }
    }

    operator fun <T> invoke(configuration: Builder<T>.() -> Unit) {
      build(configuration)
        .build()
        .execute()
    }
  }

  /**
   * A builder for a javafx worker
   */
  class Builder<T> {
    var backgroundAction: (() -> T)? = null

    var onCleanup: () -> Unit = {}

    var onSuccess: (t: T?) -> Unit = {}
    var onFailure: (e: Throwable) -> Unit = {}

    fun build(): JavaFxWorker<T> {
      val backgroundActionCopy = backgroundAction
      requireNotNull(backgroundActionCopy) {
        "No backgroundAction has been set!"
      }

      return object : JavaFxWorker<T>() {
        override fun doInBackground(): T {
          return backgroundActionCopy.invoke()
        }

        override fun cleanup() {
          super.cleanup()
          onCleanup()
        }

        override fun success(t: T) {
          super.success(t)
          onSuccess(t)
        }

        override fun failure(e: Throwable) {
          super.failure(e)
          onFailure(e)
        }
      }
    }
  }
}
