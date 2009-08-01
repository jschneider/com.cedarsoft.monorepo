package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Redirects a stream into another
 */
public class OutputRedirector implements Runnable {
  /**
   * Redirects the output of the given process to {@link System#out} and
   * {@link System#err}
   *
   * @param process the process
   */
  public static void redirect( @NotNull Process process ) {
    PrintStream targetOut = System.out;
    PrintStream targetErr = System.err;
    redirect( process, targetOut, targetErr );
  }

  public static void redirect( @NotNull Process process, @NotNull OutputStream targetOut, @NotNull OutputStream targetErr ) {
    new Thread( new OutputRedirector( process.getInputStream(), targetOut ) ).start();
    new Thread( new OutputRedirector( process.getErrorStream(), targetErr ) ).start();
  }

  private final InputStream in;
  private final OutputStream out;

  /**
   * Redirect the given input stream to the output stream
   *
   * @param in  the input stream
   * @param out the output stream
   */
  public OutputRedirector( @NotNull InputStream in, @NotNull OutputStream out ) {
    this.in = in;
    this.out = out;
  }

  public void run() {
    try {
      BufferedInputStream inputStream = null;
      try {
        inputStream = new BufferedInputStream( in );
        int c;
        while ( ( c = inputStream.read() ) > -1 ) {
          out.write( ( char ) c );
        }
      } finally {
        if ( inputStream != null ) {
          inputStream.close();
        }
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}
