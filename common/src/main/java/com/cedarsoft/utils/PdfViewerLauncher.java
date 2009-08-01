package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.JOptionPane;

/**
 *
 */
public class PdfViewerLauncher {
  private static final String ERR_MSG = "Error attempting to launch pdf viewer";

  /**
   * Opens a browser
   *
   * @param file the url that is opened within the browser
   */
  public static void openFile( @NotNull @NonNls String file ) {
    String osName = System.getProperty( "os.name" );
    try {
      if ( osName.startsWith( "Mac OS" ) ) {
        throw new UnsupportedOperationException( "Not implemented for Mac OS yet" );
        //        Class<?> fileMgr = Class.forName( "com.apple.eio.FileManager" );
        //        Method openFile = fileMgr.getDeclaredMethod( "openFile", new Class[]{String.class} );
        //        openFile.invoke( null, file );
      } else if ( osName.startsWith( "Windows" ) ) {
        Runtime.getRuntime().exec( "start \"" + file + "\"" );
      } else {
        //assume Unix or Linux
        String[] bins = {"acroread", "evince"};
        String browser = null;
        for ( int count = 0; count < bins.length && browser == null; count++ ) {
          if ( Runtime.getRuntime().exec( new String[]{"which", bins[count]} ).waitFor() == 0 ) {
            browser = bins[count];
          }
        }
        if ( browser == null ) {
          throw new Exception( "Could not find pdf viewer" );
        } else {
          Runtime.getRuntime().exec( new String[]{browser, file} );
        }
      }
    } catch ( Exception e ) {
      JOptionPane.showMessageDialog( null, ERR_MSG + ":\n" + e.getLocalizedMessage() );
    }
  }
}
