package com.cedarsoft.serialization;

import com.cedarsoft.serialization.DateTimeSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import static org.testng.Assert.assertEquals;

/**
 *
 */
public class DateTimeSerializer2Test extends CollustraAbstractSerializerTest<DateTime> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<DateTime> getSerializer() {
    return new DateTimeSerializer();
  }

  @NotNull
  @Override
  protected DateTime createObjectToSerialize() {
    return new DateTime( 2009, 5, 1, 2, 2, 5, 4 );
  }

  @NotNull
  @Override
  protected String getExpectedSerializedString() {
    return "<dateTime>20090501T020205.004-0400</dateTime>";
  }

  @Override
  protected void verifyDeserialized( @NotNull DateTime dateTime ) {
    Assert.assertEquals( dateTime, createObjectToSerialize() );
  }
}
