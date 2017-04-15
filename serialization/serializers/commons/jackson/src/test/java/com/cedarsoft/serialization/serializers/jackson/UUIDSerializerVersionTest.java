package com.cedarsoft.serialization.serializers.jackson;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.junit.experimental.theories.*;

import com.cedarsoft.serialization.StreamSerializer;
import com.cedarsoft.serialization.test.utils.AbstractJsonVersionTest2;
import com.cedarsoft.serialization.test.utils.VersionEntry;
import com.cedarsoft.version.Version;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UUIDSerializerVersionTest extends AbstractJsonVersionTest2<UUID> {
  @Nonnull
  @DataPoint
  public static final VersionEntry ENTRY1 = AbstractJsonVersionTest2.create(
    Version.valueOf(1, 0, 0), UUIDSerializerVersionTest.class.getResource("UUID_1.0.0_1.json"));

  @Nonnull
  @Override
  protected StreamSerializer<UUID> getSerializer() throws Exception {
    return new UUIDSerializer();
  }

  @Override
  protected void verifyDeserialized(@Nonnull UUID deserialized, @Nonnull Version version) {
    assertEquals("e7fa0223-be35-4c8b-a205-788d4b0db1cb", deserialized.toString());
  }
}
