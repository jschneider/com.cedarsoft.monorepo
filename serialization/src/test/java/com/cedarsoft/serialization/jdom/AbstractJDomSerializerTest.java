package com.cedarsoft.serialization.jdom;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static org.testng.Assert.*;

import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public abstract class AbstractJDomSerializerTest<T> {
  @Test
  public void testSerializer() throws IOException {
    AbstractJDomSerializer<T> serializer = getSerializer();

    T objectToSerialize = createObjectToSerialize();

    byte[] serialized = serializer.serialize( objectToSerialize );
    assertEquals( new String( serialized ).trim(), getExpectedSerializedString().trim() );


    T deserialized = serializer.deserialize( new ByteArrayInputStream( serialized ) );

    verifyDeserialized( deserialized );
  }

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @NotNull
  protected abstract AbstractJDomSerializer<T> getSerializer();

  /**
   * Creates the object to serialize
   *
   * @return the object to serialize
   */
  @NotNull
  protected abstract T createObjectToSerialize();

  /**
   * Returns the expected serialized string
   *
   * @return the expected serialized string
   */
  @NotNull
  @NonNls
  protected abstract String getExpectedSerializedString();

  /**
   * Verifies the deserialized object
   *
   * @param deserialized the deserialized object
   */
  protected abstract void verifyDeserialized( @NotNull T deserialized );

}
