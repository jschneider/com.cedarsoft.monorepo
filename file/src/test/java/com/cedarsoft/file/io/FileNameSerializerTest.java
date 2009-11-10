package com.cedarsoft.file.io;

import com.cedarsoft.file.FileName;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializerTest;
import org.jetbrains.annotations.NotNull;

import static org.testng.Assert.*;

/**
 *
 */
public class FileNameSerializerTest extends AbstractStaxMateSerializerTest<FileName> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<FileName> getSerializer() {
    return new FileNameSerializer();
  }

  @NotNull
  @Override
  protected FileName createObjectToSerialize() {
    return new FileName( "a", ",", "pdf" );
  }

  @NotNull
  @Override
  protected String getExpectedSerializedString() {
    return "<fileName><baseName>a</baseName>" +
      "<delimiter>,</delimiter>" +
      "<extension>pdf</extension></fileName>";
  }

  @Override
  protected void verifyDeserialized( @NotNull FileName fileName ) {
    assertEquals( fileName, createObjectToSerialize() );
  }
}
