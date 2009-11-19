package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class VersionMismatchException extends VersionProblemExpection {
  @NotNull
  private final Version expected;

  @NotNull
  private final Version actual;

  public VersionMismatchException( @NotNull Version expected, @NotNull Version actual ) {
    super( "Version mismatch. Expected <" + expected + "> but was <" + actual + ">" );
    this.expected = expected;
    this.actual = actual;
  }

  @NotNull
  public Version getExpected() {
    return expected;
  }

  @NotNull
  public Version getActual() {
    return actual;
  }
}
