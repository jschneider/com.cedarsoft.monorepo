package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.VersionMismatchException;
import com.cedarsoft.VersionRange;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializerTest;
import com.cedarsoft.utils.XmlCommons;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.testng.Assert.*;

/**
 *
 */
public class StaxMateSerializerTest extends AbstractStaxMateSerializerTest<String, Lookup> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<String, Lookup> getSerializer() {
    return new AbstractStaxMateSerializer<String, Lookup>( "aString", new VersionRange( new Version( 1, 5, 3 ), new Version( 1, 5, 3 ) ) ) {
      @Override
      @NotNull
      public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull String object, @NotNull Lookup context ) throws XMLStreamException {
        serializeTo.addCharacters( object );
        return serializeTo;
      }

      @Override
      @NotNull
      public String deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws XMLStreamException {
        deserializeFrom.next();
        String text = deserializeFrom.getText();
        closeTag( deserializeFrom );
        return text;
      }
    };
  }

  @Override
  protected void verifySerialized( @NotNull byte[] serialized ) throws SAXException, IOException {
    super.verifySerialized( serialized );
    assertTrue( new String( serialized ).contains( "<?format 1.5.3?>" ), XmlCommons.format( new String( serialized ) ) );
  }

  @NotNull
  @Override
  protected String createObjectToSerialize() {
    return "asdf";
  }

  @NotNull
  @Override
  protected String getExpectedSerialized() {
    return "<aString>asdf</aString>";
  }

  @Override
  protected void verifyDeserialized( @NotNull String deserialized ) {
    assertEquals( deserialized, "asdf" );
  }

  @Test
  public void testnoVersion() throws IOException {
    try {
      getSerializer().deserialize( new ByteArrayInputStream( "<aString>asdf</aString>".getBytes() ) );
      fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {

    }
  }

  @Test
  public void testWrongVersion() throws IOException {
    try {
      getSerializer().deserialize( new ByteArrayInputStream( "<?format 0.9.9?><aString>asdf</aString>".getBytes() ) );
      fail( "Where is the Exception" );
    } catch ( VersionMismatchException ignore ) {

    }
  }
}
