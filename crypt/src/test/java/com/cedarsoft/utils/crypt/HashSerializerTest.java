package com.cedarsoft.utils.crypt;

import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializerTest;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class HashSerializerTest extends AbstractStaxMateSerializerTest<Hash> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<Hash> getSerializer() {
    return new HashSerializer();
  }

  @NotNull
  @Override
  protected Hash createObjectToSerialize() {
    return Hash.fromHex( Algorithm.SHA256, "11223344" );
  }

  @NotNull
  @Override
  protected String getExpectedSerialized() {
    return "<hash algorithm=\"SHA256\">11223344</hash>";
  }
}
