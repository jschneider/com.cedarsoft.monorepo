package com.cedarsoft.serialization.stax;

import com.cedarsoft.lookup.Lookup;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 *
 */
public class StaxMateSerializerTest extends AbstractStaxMateSerializerTest<String> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<String> getSerializer() {
    return new AbstractStaxMateSerializer<String>( "aString" ) {
      @NotNull
      @Override
      public SMOutputElement serialize( @NotNull SMOutputElement element, @NotNull String object, @NotNull Lookup context ) throws IOException, XMLStreamException {
        element.addCharacters( object );
        return element;
      }

      @NotNull
      @Override
      public String deserialize( @NotNull XMLStreamReader2 reader, @NotNull Lookup context ) throws IOException, XMLStreamException {
        reader.next();
        return reader.getText();
      }
    };
  }

  @NotNull
  @Override
  protected String createObjectToSerialize() {
    return "asdf";
  }

  @NotNull
  @Override
  protected String getExpectedSerializedString() {
    return "<aString>asdf</aString>";
  }

  @Override
  protected void verifyDeserialized( @NotNull String deserialized ) {
    assertEquals( deserialized, "asdf" );
  }
}
