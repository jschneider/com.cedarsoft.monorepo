package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The browser launches the default browser for the current platform.
 */
@Deprecated
public class BrowserLauncher {
  //  private static final String ERR_MSG = "Error attempting to launch web browser";

  /**
   * Opens a browser
   *
   * @param url the url that is opened within the browser
   */
  public static void openURL( @NotNull @NonNls String url ) {
    try {
      Desktop.getDesktop().browse( new URI( url ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    } catch ( URISyntaxException e ) {
      throw new RuntimeException( e );
    }

    //    String osName = System.getProperty( "os.name" );
    //    try {
    //      if ( osName.startsWith( "Mac OS" ) ) {
    //        Class<?> fileMgr = Class.forName( "com.apple.eio.FileManager" );
    //        Method openURL = fileMgr.getDeclaredMethod( "openFile", new Class[]{String.class} );
    //        openURL.invoke( null, url );
    //      } else if ( osName.startsWith( "Windows" ) ) {
    //        Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler " + url );
    //      } else {
    //        //assume Unix or Linux
    //        String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
    //        String browser = null;
    //        for ( int count = 0; count < browsers.length && browser == null; count++ ) {
    //          if ( Runtime.getRuntime().exec( new String[]{"which", browsers[count]} ).waitFor() == 0 ) {
    //            browser = browsers[count];
    //          }
    //        }
    //        if ( browser == null ) {
    //          throw new Exception( "Could not find web browser" );
    //        } else {
    //          Runtime.getRuntime().exec( new String[]{browser, url} );
    //        }
    //      }
    //    } catch ( Exception e ) {
    //      JOptionPane.showMessageDialog( null, ERR_MSG + ":\n" + e.getLocalizedMessage() );
    //    }
  }
}
