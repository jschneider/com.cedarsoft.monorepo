package com.cedarsoft.file;

import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializerTest;
import org.jetbrains.annotations.NotNull;

import static org.testng.Assert.*;

/**
 *
 */
public class ExtensionSerializerTest extends AbstractStaxMateSerializerTest<Extension, Object> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<Extension, Object> getSerializer() {
    return new ExtensionSerializer();
  }

  @NotNull
  @Override
  protected Extension createObjectToSerialize() {
    return new Extension( ",", "jpg" );
  }

  @NotNull
  @Override
  protected String getExpectedSerialized() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<extension delimiter=\",\">jpg</extension>";
  }

  @Override
  protected void verifyDeserialized( @NotNull Extension extension ) {
    assertEquals( new Extension( ",", "jpg" ), extension );
  }
}
