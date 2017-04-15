package com.cedarsoft.serialization.serializers.jackson;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.cedarsoft.serialization.jackson.AbstractJacksonSerializer;
import com.cedarsoft.serialization.jackson.StringSerializer;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UUIDSerializer extends AbstractJacksonSerializer<UUID> {
  @Inject
  public UUIDSerializer() {
    super("uuid", VersionRange.from(1, 0, 0).to());
  }

  @Override
  public boolean isObjectType() {
    return false;
  }

  @Override
  public void serialize(@Nonnull JsonGenerator serializeTo, @Nonnull UUID object, @Nonnull Version formatVersion) throws IOException, VersionException, JsonProcessingException {
    verifyVersionWritable(formatVersion);
    serializeTo.writeString(object.toString());
  }

  @Override
  @Nonnull
  public UUID deserialize(@Nonnull JsonParser deserializeFrom, @Nonnull Version formatVersion) throws IOException, VersionException, JsonProcessingException {
    verifyVersionReadable(formatVersion);
    String uuidAsString = deserializeFrom.getText().trim();

    return UUID.fromString(uuidAsString);
  }
}
