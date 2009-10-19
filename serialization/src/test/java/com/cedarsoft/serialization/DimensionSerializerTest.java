package com.cedarsoft.serialization;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;

import java.awt.Dimension;

/**
 *
 */
public class DimensionSerializerTest extends AbstractSerializerTest<Dimension> {
  @NotNull
  @Override
  protected AbstractSerializer<Dimension> getSerializer() {
    return new DimensionSerializer();
  }

  @NotNull
  @Override
  protected Dimension createObjectToSerialize() {
    return new Dimension( 1600, 600 );
  }

  @NotNull
  @Override
  protected String getExpectedSerializedString() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<dimension>1600x600</dimension>";
  }

  @Override
  protected void verifyDeserialized( @NotNull Dimension deserialized ) {
    assertEquals( deserialized.width, 1600 );
    assertEquals( deserialized.height, 600 );
  }
}
