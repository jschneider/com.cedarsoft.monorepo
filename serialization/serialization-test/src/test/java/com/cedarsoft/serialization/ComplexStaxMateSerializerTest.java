package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializerTest;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.testng.Assert.*;

/**
 *
 */
public class ComplexStaxMateSerializerTest extends AbstractStaxMateSerializerTest<String, Lookup> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<String, Lookup> getSerializer() {
    final AbstractStaxMateSerializer<String, Lookup> stringSerializer = new AbstractStaxMateSerializer<String, Lookup>( "asdf", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) ) {
      @Override
      @NotNull
      public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull String object, @Nullable Lookup context ) throws XMLStreamException {
        serializeTo.addCharacters( object );
        return serializeTo;
      }

      @Override
      @NotNull
      public String deserialize( @NotNull XMLStreamReader2 deserializeFrom, @Nullable Lookup context ) throws XMLStreamException {
        deserializeFrom.next();
        String text = deserializeFrom.getText();
        closeTag( deserializeFrom );
        return text;
      }
    };

    return new AbstractStaxMateSerializer<String, Lookup>( "aString", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) ) {
      @Override
      @NotNull
      public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull String object, @Nullable Lookup context ) throws IOException, XMLStreamException {
        stringSerializer.serialize( serializeTo.addElement( "sub" ), object, context );
        serializeTo.addElement( "emptyChild" ).addCharacters( "" );

        return serializeTo;
      }

      @Override
      @NotNull
      public String deserialize( @NotNull XMLStreamReader2 deserializeFrom, @Nullable Lookup context ) throws IOException, XMLStreamException {
        nextTag( deserializeFrom, "sub" );
        String string = stringSerializer.deserialize( deserializeFrom, context );

        assertEquals( getChildText( deserializeFrom, "emptyChild" ), "" );
        closeTag( deserializeFrom );

        return string;
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
  protected String getExpectedSerialized() {
    return "<aString><sub>asdf</sub><emptyChild/></aString>";
  }
}