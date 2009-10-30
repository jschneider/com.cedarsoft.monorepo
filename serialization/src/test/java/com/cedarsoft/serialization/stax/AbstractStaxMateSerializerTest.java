package com.cedarsoft.serialization.stax;

import com.cedarsoft.AssertUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * @param <T> the type
 */
public abstract class AbstractStaxMateSerializerTest<T> {
  @Test
  public void testSerializer() throws IOException, SAXException {
    AbstractStaxMateSerializer<T> serializer = getSerializer();

    T objectToSerialize = createObjectToSerialize();

    byte[] serialized = serializer.serialize( objectToSerialize );
    AssertUtils.assertXMLEqual( "Was ---\n" + new String( serialized ) + "\n---", new String( serialized ), getExpectedSerializedString(), true );

    T deserialized = serializer.deserialize( new ByteArrayInputStream( serialized ) );

    verifyDeserialized( deserialized );
  }

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @NotNull
  protected abstract AbstractStaxMateSerializer<T> getSerializer();

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
