package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Johannes Schneider
 */
public class DummyCmdLine extends AbstractCmdLine {
  private PrintStream out;

  public DummyCmdLine() {
    this( System.out );
  }

  public DummyCmdLine( @NotNull PrintStream out ) {
    this.out = out;
  }

  public boolean readBoolean( @NotNull String message ) throws IOException {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public String read( @NotNull String message ) {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public String read( @NotNull String message, @Nullable String defaultValue ) {
    throw new UnsupportedOperationException();
  }

  public int readInt( @NotNull String message, int lower, int upper ) {
    throw new UnsupportedOperationException();
  }

  public int readInt( @NotNull String message ) throws IOException {
    throw new UnsupportedOperationException();
  }

  public void pause( int seconds ) {
  }

  @Override
  @NotNull
  public PrintStream getOut() {
    return out;
  }

  @Override
  @NotNull
  protected ConsolePrinter getConsolePrinter() {
    return new DefaultConsolePrinter();
  }
}
