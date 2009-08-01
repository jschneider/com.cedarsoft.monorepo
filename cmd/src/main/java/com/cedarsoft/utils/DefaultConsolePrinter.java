package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/**
 * <p/>
 * Date: 25.08.2006<br>
 * Time: 00:59:19<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class DefaultConsolePrinter implements ConsolePrinter {
  @NotNull
  public String createError( @NotNull String message, @NotNull Object... objects ) {
    StringBuilder sb = new StringBuilder();
    String toPrint = MessageFormat.format( message, objects );
    for ( int i = 0; i < toPrint.length(); i++ ) {
      sb.append( "#" );
    }
    sb.append( toPrint );
    sb.append( "\n" );
    for ( int i = 0; i < toPrint.length(); i++ ) {
      sb.append( "#" );
    }

    return sb.toString();
  }

  @NotNull
  public String createWarning( @NotNull String message, @NotNull Object... objects ) {
    StringBuilder sb = new StringBuilder();
    String toPrint = MessageFormat.format( message, objects );

    sb.append( toPrint );
    sb.append( "\n" );
    for ( int i = 0; i < toPrint.length(); i++ ) {
      sb.append( "#" );
    }

    return sb.toString();
  }

  @NotNull
  public String createSuccess( @NotNull String message, @NotNull Object... objects ) {
    StringBuilder sb = new StringBuilder();
    String toPrint = MessageFormat.format( message, objects );
    sb.append( "!!!" );
    sb.append( toPrint );

    return sb.toString();
  }
}
