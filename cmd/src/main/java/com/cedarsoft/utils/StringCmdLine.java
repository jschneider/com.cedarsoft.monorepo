package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.Override;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command Line implementation that is based on strings.
 * <p/>
 * Date: 25.09.2006<br>
 * Time: 19:43:04<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class StringCmdLine extends AbstractCmdLine implements CmdLine {
  private int expectedIndex;
  private List<String> expectedOut = new ArrayList<String>();
  private List<Object> answers = new ArrayList<Object>();
  private ConsolePrinter consolePrinter = new SimpleConsolePrinter();

  private void checkExpectedOut( @NotNull Object given ) {
    if ( expectedOut.size() <= expectedIndex ) {
      throw new IndexOutOfBoundsException( "No entries left for: " + given );
    }

    @NonNls
    String nextExpected = this.expectedOut.get( expectedIndex );
    expectedIndex++;

    if ( nextExpected == null ) {
      return;
    }

    if ( !nextExpected.equals( String.valueOf( given ) ) ) {
      throw new RuntimeException( "Did not match: \"" + nextExpected + "\" - but was:  \"" + given + "\"" );
    }
  }

  @Override
  @NotNull
  public PrintStream getOut() {
    throw new UnsupportedOperationException();
  }

  @Override
  @NotNull
  protected ConsolePrinter getConsolePrinter() {
    return consolePrinter;
  }

  @Override
  public boolean readBoolean( @NotNull String message ) throws IOException {
    messages.add( message );
    return ( Boolean ) getNextAnswer();
  }

  @Override
  @NotNull
  public String read( @NotNull String message ) {
    messages.add( message );
    return ( String ) getNextAnswer();
  }

  private final List<String> messages = new ArrayList<String>();

  @NotNull
  public List<String> getMessages() {
    return Collections.unmodifiableList( messages );
  }

  protected Object getNextAnswer() {
    if ( answers.isEmpty() ) {
      throw new IllegalStateException( "No answer left" );
    }
    return answers.remove( 0 );
  }

  @Override
  @NotNull
  public String read( @NotNull String message, @Nullable String defaultValue ) {
    return read( message );
  }

  @Override
  public int readInt( @NotNull String message, int lower, int upper ) {
    messages.add( message );
    return ( Integer ) getNextAnswer();
  }

  @Override
  public int readInt( @NotNull String message ) throws IOException {
    messages.add( message );
    return ( Integer ) getNextAnswer();
  }

  @Override
  public void pause( int seconds ) {
  }

  @Override
  public void out( @NotNull String message, @NotNull Object... objects ) {
    messages.add( message );
    checkExpectedOut( consolePrinter.createSuccess( message, objects ) );
  }

  @Override
  public void out( @NotNull Process process ) {
    try {
      BufferedReader defaultIn = null;
      try {
        defaultIn = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
        String line;
        while ( ( line = defaultIn.readLine() ) != null ) {
          out( line );
        }
      } finally {
        if ( defaultIn != null ) {
          defaultIn.close();
        }
      }

      BufferedReader errorIn = null;
      try {
        errorIn = new BufferedReader( new InputStreamReader( process.getErrorStream() ) );
        String line;
        while ( ( line = errorIn.readLine() ) != null ) {
          error( line );
        }
      } finally {
        if ( errorIn != null ) {
          errorIn.close();
        }
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  @Override
  public void error( @NotNull String message, @NotNull Object... objects ) {
    messages.add( message );
    checkExpectedOut( getConsolePrinter().createError( message, objects ) );
  }

  @Override
  public void warning( @NotNull String message, @NotNull Object... objects ) {
    messages.add( message );
    checkExpectedOut( getConsolePrinter().createWarning( message, objects ) );
  }

  @Override
  public void success( @NotNull String message, @NotNull Object... objects ) {
    messages.add( message );
    checkExpectedOut( getConsolePrinter().createSuccess( message, objects ) );
  }

  @Override
  public void outNl() {
    checkExpectedOut( "" );
  }

  public void addExpectedOut( @Nullable String expected ) {
    expectedOut.add( expected );
  }

  @NotNull
  public List<String> getExpectedOut() {
    return Collections.unmodifiableList( expectedOut );
  }

  @NotNull
  public List<Object> getAnswers() {
    return Collections.unmodifiableList( answers );
  }

  public void setAnswers( @NotNull List<Object> answers ) {
    this.answers.clear();
    this.answers.addAll( answers );
  }

  public void addAnswer( @NotNull Object answer ) {
    answers.add( answer );
  }

  public void removeAnswer( @NotNull Object answer ) {
    answers.remove( answer );
  }

  private static class SimpleConsolePrinter implements ConsolePrinter {
    @Override
    @NotNull
    public String createError( @NotNull String message, @NotNull Object... objects ) {
      return "ERROR: " + MessageFormat.format( message, objects );
    }

    @Override
    @NotNull
    public String createWarning( @NotNull String message, @NotNull Object... objects ) {
      return "WARN: " + MessageFormat.format( message, objects );
    }

    @Override
    @NotNull
    public String createSuccess( @NotNull String message, @NotNull Object... objects ) {
      return MessageFormat.format( message, objects );
    }
  }
}
