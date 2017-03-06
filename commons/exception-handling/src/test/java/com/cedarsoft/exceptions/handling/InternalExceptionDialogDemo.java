package com.cedarsoft.exceptions.handling;

import com.cedarsoft.version.Version;
import org.junit.*;

import javax.annotation.Nonnull;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class InternalExceptionDialogDemo {
  @Before
  public void setUp() throws Exception {
    UIManager.setLookAndFeel(new NimbusLookAndFeel());
  }

  @Test
  public void basic() throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      InternalExceptionDialog dialog = new InternalExceptionDialog(null, new RuntimeException(new IllegalArgumentException("Uups")), null, Version.valueOf(1, 2, 3));
      dialog.setVisible(true);
    });
  }

  @Test
  public void basicWithExceReporter() throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      InternalExceptionDialog dialog = new InternalExceptionDialog(null, new RuntimeException(new IllegalArgumentException("Uups")), new ExceptionReporter() {
        @Override
        public void report(@Nonnull Version applicationVersion, @Nonnull Throwable throwable) {
          System.err.println("Reporting exception");
        }
      }, Version.valueOf(1, 2, 3));
      dialog.setVisible(true);
    });
  }
}
