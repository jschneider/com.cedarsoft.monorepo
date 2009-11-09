package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.Override;
import java.text.MessageFormat;
import java.util.List;

/**
 * Base class that delegates all methods to the a few core methods.
 */
public abstract class AbstractCmdLine implements CmdLine {
  @Override
  public void warning( @NotNull String message, @NotNull Object... objects ) {
    getOut().println( getConsolePrinter().createWarning( message, objects ) );
  }

  @Override
  public void error( @NotNull String message, @NotNull Object... objects ) {
    getOut().println( getConsolePrinter().createError( message, objects ) );
  }

  @Override
  public void success( @NotNull String message, @NotNull Object... objects ) {
    getOut().println( getConsolePrinter().createSuccess( message, objects ) );
  }

  @Override
  public void outNl() {
    getOut().println();
  }

  @Override
  public void out( @NotNull String message, @NotNull Object... objects ) {
    if ( objects.length == 0 ) {
      getOut().println( message );
    } else {
      getOut().println( MessageFormat.format( message, objects ) );
    }
  }

  @Override
  public void out( @NotNull Process process ) {
    try {
      BufferedReader defaultIn = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
      try {
        String line;
        while ( ( line = defaultIn.readLine() ) != null ) {
          out( line );
        }
      } finally {
        defaultIn.close();
      }

      BufferedReader errorIn = new BufferedReader( new InputStreamReader( process.getErrorStream() ) );
      try {
        String line;
        while ( ( line = errorIn.readLine() ) != null ) {
          error( line );
        }
      } finally {
        errorIn.close();
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  @Override
  @NotNull
  public <T> T readSelection( @NotNull String message, @NotNull List<? extends T> elements ) {
    return readSelection( message, elements, null );
  }

  protected <T> void printPossibleElements( @NotNull List<? extends T> elements, @Nullable Renderer<? super T, Object> presenter ) {
    if ( presenter == null ) {
      //noinspection AssignmentToMethodParameter
      presenter = new DefaultRenderer();
    }
    int index = 0;
    for ( T element : elements ) {
      out( "\t" + index + '\t' + presenter.render( element, null ) );
      index++;
    }
  }

  @Override
  @NotNull
  public <T> T readSelection( @NotNull String message, @NotNull List<? extends T> elements, @Nullable Renderer<? super T, Object> presenter ) {
    out( message );
    printPossibleElements( elements, presenter );
    return elements.get( readInt( "Enter the number of the element you want to select", 0, elements.size() ) );
  }

  @Override
  @NotNull
  public String read( @NotNull String message, @NotNull List<String> elements ) {
    out( message );
    printPossibleElements( elements, null );

    @Nullable
    String read = null;
    while ( read == null || read.length() == 0 ) {
      read = read( "Enter the value. Or enter the number of the element you want to select." );

      try {
        int selected = Integer.parseInt( read );

        if ( selected < 0 || selected > elements.size() - 1 ) {
          read = null;
          warning( "Selection invalid: <" + selected + "> " );
        } else {
          return elements.get( selected );
        }
      } catch ( NumberFormatException ignore ) {
        return read;
      }
    }

    throw new IllegalStateException( "Should not reach" );
  }

  @Override
  @NotNull
  public String read( @NotNull String message, @NotNull List<String> elements, @NotNull String preselected ) {
    out( message );
    printPossibleElements( elements, null );

    String read = read( "Enter the value. Or enter the number of the element you want to select. Default selection: " + preselected );

    if ( read.length() == 0 ) {
      return preselected;
    } else {
      return read;
    }
  }

  @Override
  @NotNull
  public <T> T read( @NotNull String message, @NotNull List<? extends T> elements, @Nullable Renderer<T, Object> presenter, @NotNull ObjectFactory<T> objectFactory ) {
    out( message );
    printPossibleElements( elements, presenter );

    String read = null;

    while ( read == null ) {
      read = read( "Enter the value. Or enter the number of the element you want to select." );
      try {
        int index = Integer.parseInt( read );
        if ( index >= 0 && index < elements.size() ) {
          return elements.get( index );
        }
      } catch ( NumberFormatException ignore ) {
      }

      try {
        return objectFactory.create( read );
      } catch ( ObjectFactory.InvalidRepresentationException e ) {
        this.error( "Invalid input: " + e.getMessage() );
        //noinspection AssignmentToNull
        read = null;
      }
    }

    throw new IllegalStateException( "Should not reach" );
  }

  @NotNull
  public abstract PrintStream getOut();

  @NotNull
  protected abstract ConsolePrinter getConsolePrinter();
}
