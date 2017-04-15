package com.cedarsoft.serialization.serializers.jackson;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.junit.experimental.theories.*;

import com.cedarsoft.serialization.StreamSerializer;
import com.cedarsoft.serialization.jackson.StringSerializer;
import com.cedarsoft.serialization.test.utils.AbstractJsonSerializerTest2;
import com.cedarsoft.serialization.test.utils.AbstractSerializerTest2;
import com.cedarsoft.serialization.test.utils.Entry;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UUIDSerializerTest extends AbstractJsonSerializerTest2<UUID> {
  @Nonnull
  @DataPoint
  public static final Entry<? extends UUID> ENTRY1 = AbstractSerializerTest2.create(
    UUID.fromString("e7fa0223-be35-4c8b-a205-788d4b0db1cb"),
    UUIDSerializerTest.class.getResource("UUID_1.0.0_1.json"));

  @Nonnull
  @Override
  protected StreamSerializer<UUID> getSerializer() throws Exception {
    return new UUIDSerializer();
  }
}
