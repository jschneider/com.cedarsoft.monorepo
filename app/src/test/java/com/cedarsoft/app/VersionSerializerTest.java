package com.cedarsoft.app;

import com.cedarsoft.serialization.jdom.AbstractJDomSerializerTest;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializerTest;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;

import static org.testng.Assert.*;

/**
 *
 */
public class VersionSerializerTest extends AbstractStaxMateSerializerTest<Version> {
  @Override
  @NotNull
  protected VersionSerializer getSerializer() {
    return new VersionSerializer();
  }

  @Override
  @NotNull
  protected Version createObjectToSerialize() {
    return new Version( 1, 2, 3, "build65" );
  }

  @Override
  @NotNull
  protected String getExpectedSerializedString() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<version>1.2.3-build65</version>";
  }
}
