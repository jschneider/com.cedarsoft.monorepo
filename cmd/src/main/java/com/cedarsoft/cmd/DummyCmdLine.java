package com.cedarsoft.cmd;

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

  @Override
  public boolean readBoolean( @NotNull String message ) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  @NotNull
  public String read( @NotNull String message ) {
    throw new UnsupportedOperationException();
  }

  @Override
  @NotNull
  public String read( @NotNull String message, @Nullable String defaultValue ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int readInt( @NotNull String message, int lower, int upper ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int readInt( @NotNull String message ) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
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
