package com.cedarsoft.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.annotations.*;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.testng.Assert.*;

/**
 * Abstract class for serializer tests
 *
 * @param <T> the type of the serialized object
 * @param <C> the context type
 */
public abstract class AbstractSerializerTest<T, C> {
  @Test
  public void testSerializer() throws IOException, SAXException {
    Serializer<T, C> serializer = getSerializer();

    T objectToSerialize = createObjectToSerialize();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    serializer.serialize( objectToSerialize, out, createSerializeContext() );

    byte[] serialized = out.toByteArray();
    verifySerialized( serialized );

    T deserialized = serializer.deserialize( new ByteArrayInputStream( serialized ), createDeserializeContext() );

    verifyDeserialized( deserialized );
  }

  /**
   * Creates the context to serialize
   *
   * @return the context to serialize
   */
  @Nullable
  protected C createSerializeContext() {
    return null;
  }


  /**
   * Creates the context to deserialize
   *
   * @return the context to deserialize
   */
  @Nullable
  protected C createDeserializeContext() {
    return null;
  }

  /**
   * Verifies the serialized object
   *
   * @param serialized the serialized object
   * @throws SAXException
   * @throws IOException
   */
  protected abstract void verifySerialized( @NotNull byte[] serialized ) throws SAXException, IOException;

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @NotNull
  protected abstract Serializer<T, C> getSerializer();

  /**
   * Creates the object to serialize
   *
   * @return the object to serialize
   */
  @NotNull
  protected abstract T createObjectToSerialize();

  /**
   * Verifies the deserialized object.
   * The default implementation simply calls equals
   *
   * @param deserialized the deserialized object
   */
  protected void verifyDeserialized( @NotNull T deserialized ) {
    assertEquals( deserialized, createObjectToSerialize() );
  }
}
