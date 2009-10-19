package com.cedarsoft.app;

import com.cedarsoft.app.ApplicationSerializer;
import com.cedarsoft.app.Version;
import com.cedarsoft.app.VersionSerializer;
import com.cedarsoft.collustra.processing.Application;
import com.cedarsoft.serialization.AbstractSerializerTest;
import com.cedarsoft.serialization.AbstractSerializer;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class ApplicationSerializerTest extends AbstractSerializerTest<Application> {
  @NotNull
  @Override
  protected AbstractSerializer<Application> getSerializer() {
    return new ApplicationSerializer( new VersionSerializer() );
  }

  @NotNull
  @Override
  protected Application createObjectToSerialize() {
    return new Application( "gimp", new Version( 1, 2, 3 ) );
  }

  @NotNull
  @Override
  protected String getExpectedSerializedString() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<application>\n" +
      "  <name>gimp</name>\n" +
      "  <version>1.2.3</version>\n" +
      "</application>";
  }

  @Override
  protected void verifyDeserialized( @NotNull Application application ) {
    assertEquals( new Application( "gimp", new Version( 1, 2, 3 ) ), application );
  }
}
