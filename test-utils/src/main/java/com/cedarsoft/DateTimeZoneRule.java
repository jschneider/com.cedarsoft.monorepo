package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTimeZone;
import org.junit.rules.*;
import org.junit.runners.model.*;

/**
 * Rule that sets the TimeZone
 */
public class DateTimeZoneRule implements MethodRule {
  @NotNull
  protected final DateTimeZone zone;

  public DateTimeZoneRule() throws IllegalArgumentException {
    this( "America/New_York" );
  }

  public DateTimeZoneRule( @NotNull @NonNls String zoneId ) throws IllegalArgumentException {
    this( DateTimeZone.forID( zoneId ) );
  }

  public DateTimeZoneRule( @NotNull DateTimeZone zone ) {
    this.zone = zone;
  }

  private DateTimeZone oldTimeZone;

  @Override
  public Statement apply( final Statement base, FrameworkMethod method, Object target ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        before();
        try {
          base.evaluate();
        } finally {
          after();
        }
      }
    };
  }

  private void before() {
    oldTimeZone = DateTimeZone.getDefault();
    DateTimeZone.setDefault( zone );
  }

  private void after() {
    DateTimeZone.setDefault( oldTimeZone );
  }
}
