package com.cedarsoft.cmd;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.Override;

/**
 * The default command line
 */
@Singleton
public class DefaultCmdLine extends AbstractCmdLine {
  public static boolean isAnsiAware() {
    String osName = System.getProperty( "os.name" );
    if ( osName.contains( "indows" ) ) {
      return false;
    }

    //Within IDE?
    return !Boolean.parseBoolean( System.getProperty( "ansiDisabled" ) );
  }

  private final ConsolePrinter consolePrinter;

  public DefaultCmdLine() {
    if ( isAnsiAware() ) {
      this.consolePrinter = new AnsiAwareConsolePrinter();
    } else {
      this.consolePrinter = new DefaultConsolePrinter();
    }
  }

  @NotNull
  @Override
  protected ConsolePrinter getConsolePrinter() {
    return consolePrinter;
  }

  @Override
  public boolean readBoolean( @NotNull String message ) throws IOException {
    String answer = read( message + " (y/n)" );
    if ( answer.equalsIgnoreCase( "y" ) ) {
      return true;
    }
    if ( answer.equalsIgnoreCase( "j" ) ) {
      return true;
    }
    if ( answer.equalsIgnoreCase( "1" ) ) {
      return true;
    }
    return Boolean.parseBoolean( message );
  }

  @Override
  @NotNull
  public String read( @NotNull String message ) {
    return read( message, ( String ) null );
  }

  @Override
  @NotNull
  public String read( @NotNull String message, @Nullable String defaultValue ) {
    if ( defaultValue == null ) {
      out( message );
    } else {
      out( message + " [" + defaultValue + ']' );
    }

    byte[] buffer = new byte[80];
    //noinspection ResultOfMethodCallIgnored
    try {
      getIn().read( buffer, 0, 80 );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
    String read = new String( buffer ).trim();
    if ( read.length() == 0 && defaultValue != null ) {
      return defaultValue;
    }
    return read;
  }

  @NotNull
  public InputStream getIn() {
    return System.in;
  }

  @Override
  @NotNull
  public PrintStream getOut() {
    return System.out;
  }

  @Override
  public int readInt( @NotNull String message, int lower, int upper ) {
    try {
      while ( true ) {
        int value = readInt( message );
        if ( value < lower || value >= upper ) {
          out( "Value out of range. Please enter an int between " + lower + " (incl) and " + upper + " (excl)." );
        } else {
          return value;
        }
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  @Override
  public int readInt( @NotNull String message ) throws IOException {
    while ( true ) {
      String value = read( message );
      try {
        return Integer.parseInt( value );
      } catch ( NumberFormatException ignore ) {
        //noinspection HardCodedStringLiteral
        out( "Invalid argument. Please enter an Integer" );
      }
    }
  }

  @Override
  public void pause( int seconds ) {
    getOut().print( "Pausing for " + seconds + " seconds:" );
    for ( int i = 1; i <= seconds; i++ ) {
      getOut().print( " " );
      getOut().print( i );
      try {
        Thread.sleep( 1000 );
      } catch ( InterruptedException e ) {
        System.exit( 0 );
      }
    }
    getOut().println();
  }
}
