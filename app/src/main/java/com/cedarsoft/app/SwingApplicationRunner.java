package com.cedarsoft.app;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.SplashScreen;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SwingApplicationRunner {
  @MainFrame
  @Nonnull
  private JFrame mainFrame;
  @Nonnull
  private final Application application;

  public SwingApplicationRunner( @Nonnull Application application ) {
    this.application = application;
    application.prepareExceptionHandling();
    application.prepare();
  }

  public void boot() {
    SwingUtilities.invokeLater( new Runnable() {
      @Override
      public void run() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if ( splash == null ) {
          return;
        }

        application.updateSplash( splash );
      }
    } );

    mainFrame = application.createFrame();

    SwingUtilities.invokeLater( new Runnable() {
      @Override
      public void run() {
        try {
          showFrame( mainFrame );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    } );
  }

  @UiThread
  private void showFrame( @Nonnull final JFrame frame ) throws IOException {
    application.prepareFrameForShowing( frame );
    frame.pack();
    application.setSizeAndLocation( frame );
    frame.setVisible( true );
  }

  @Nonnull
  @MainFrame
  public JFrame getMainFrame() {
    return mainFrame;
  }


  public interface Application {
    /**
     * Prepares the exception handling - e.g. registering a default exception handler
     */
    @NonUiThread
    void prepareExceptionHandling();

    /**
     * Prepares the application.
     * E.g. should create the injector and return the frame (Guice: Key.get( JFrame.class, MainFrame.class ))
     */
    @NonUiThread
    void prepare();

    /**
     * Is only called if there is a splash screen
     *
     * @param splash the splash
     */
    @UiThread
    void updateSplash( @Nonnull SplashScreen splash );

    /**
     * Creates the frame.
     * This method is called from the background thread. Do *not* call pack() or setVisible()
     *
     * @return the created frame
     */
    @NonUiThread
    @Nonnull
    JFrame createFrame();

    /**
     * Is called before the frame is shown.
     *
     * @param frame the frame
     */
    @UiThread
    void prepareFrameForShowing( @Nonnull JFrame frame );

    /**
     * Sets the size and location for the given frame
     *
     * @param frame the frame
     */
    @UiThread
    void setSizeAndLocation( @Nonnull JFrame frame );
  }

  /**
   * Abstract base class for application
   */
  public abstract static class AbstractApplication implements Application {
    @Override
    public void prepareFrameForShowing( @Nonnull JFrame frame ) {
      frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
    }

    @Override
    public void setSizeAndLocation( @Nonnull JFrame frame ) {
      frame.setSize( 1024, 768 );
      frame.setLocationRelativeTo( null );
    }
  }

}
