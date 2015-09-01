package com.cedarsoft.test.utils;

import java.time.ZoneId;
import java.util.TimeZone;

import javax.annotation.Nonnull;

import org.fest.reflect.core.Reflection;
import org.junit.rules.*;
import org.junit.runners.model.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JavaTimeZoneRule implements MethodRule {
  @Nonnull
  protected final ZoneId zone;

  public JavaTimeZoneRule() throws IllegalArgumentException {
    this("America/New_York");
  }

  public JavaTimeZoneRule(@Nonnull String zoneId) throws IllegalArgumentException {
    this(ZoneId.of(zoneId));
  }

  public JavaTimeZoneRule(@Nonnull ZoneId zone) {
    this.zone = zone;
  }

  private ZoneId oldTimeZone;

  @Override
  public Statement apply(final Statement base, FrameworkMethod method, Object target) {
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
    oldTimeZone = TimeZone.getDefault().toZoneId();
    TimeZone.setDefault(TimeZone.getTimeZone(zone));
  }

  private void after() {
    TimeZone.setDefault(TimeZone.getTimeZone(oldTimeZone));
  }

  @Nonnull
  public ZoneId getZone() {
    return zone;
  }

  @Nonnull
  public ZoneId getOldTimeZone() {
    if (oldTimeZone == null) {
      throw new IllegalStateException("No old zone set");
    }
    return oldTimeZone;
  }
}