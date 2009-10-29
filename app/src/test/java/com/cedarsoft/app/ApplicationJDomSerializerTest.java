package com.cedarsoft.app;

import com.cedarsoft.serialization.jdom.AbstractJDomSerializerTest;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializer;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;

/**
 *
 */
public class ApplicationJDomSerializerTest extends AbstractJDomSerializerTest<Application> {
  @NotNull
  @Override
  protected AbstractJDomSerializer<Application> getSerializer() {
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
