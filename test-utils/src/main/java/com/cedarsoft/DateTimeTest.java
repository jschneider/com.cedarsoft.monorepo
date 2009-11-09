package com.cedarsoft;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTimeZone;
import org.testng.annotations.*;

/**
 *
 */
public class DateTimeTest {
  @NotNull
  protected final DateTimeZone zone = DateTimeZone.forID( "America/New_York" );

  private DateTimeZone oldTimeZone;

  @BeforeMethod
  protected void setUpDateTimeZone() throws Exception {
    oldTimeZone = DateTimeZone.getDefault();
    DateTimeZone.setDefault( zone );
  }

  @AfterMethod
  protected void tearDownDateTimeZone() {
    DateTimeZone.setDefault( oldTimeZone );
  }

  @Test
  public void testDummy() {
  }
}
