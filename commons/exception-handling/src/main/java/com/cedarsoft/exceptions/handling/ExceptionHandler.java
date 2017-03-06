package com.cedarsoft.exceptions.handling;

import com.cedarsoft.exceptions.CanceledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Handles exceptions
 */
public abstract class ExceptionHandler implements Thread.UncaughtExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class.getName());

  /**
   * Registers the exception handler for all threads.
   */
  @PostConstruct
  public void registerExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  @Override
  public final void uncaughtException(Thread t, Throwable e) {
    try {
      Throwable purged = ExceptionPurger.purge(e);
      LOG.info("Handling Exception", e);

      handle(t, purged, e);
    } catch (CanceledException ignore) {
    } catch (Throwable throwable) {
      LOG.error("could not handle exception", throwable);
      LOG.error("original exception", e);

      //Last resort if exception handling no longer works
      try {
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(null, "Internal problem. Please contact our technical support.\nThe application will be closed.\n\n" + extractDetailsFromThrowable(e), "Internal Problem", JOptionPane.ERROR_MESSAGE);
          System.exit(1);
        });
      } catch (Exception lastException) {
        LOG.error("Could not show last exception dialog", lastException);
      }
    }
  }

  /**
   * Extracts the details if possible
   */
  @Nonnull
  private static String extractDetailsFromThrowable(@Nonnull Throwable t) {
    try {
      return t.getClass().getName() + ": " + t.getMessage();
    } catch (Throwable ignore) {
    }
    return "<undefined>";
  }

  /**
   * Handles the given throwable
   *
   * @param thread    the thread the throwable has occurred in
   * @param throwable the throwable (purged)
   * @param original  the original throwable
   */
  protected abstract void handle(@Nonnull Thread thread, @Nonnull Throwable throwable, @Nonnull Throwable original);
}
