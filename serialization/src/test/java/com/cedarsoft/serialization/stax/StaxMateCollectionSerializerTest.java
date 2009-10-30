package com.cedarsoft.serialization.stax;

import com.cedarsoft.lookup.Lookup;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 *
 */
public class StaxMateCollectionSerializerTest extends AbstractStaxMateSerializerTest<List<String>> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<List<String>> getSerializer() {
    return new AbstractStaxMateSerializer<List<String>>( "aString" ) {
      @NotNull
      public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull List<String> object, @NotNull Lookup context ) throws XMLStreamException {
        for ( String s : object ) {
          serializeTo.addElement( "string" ).addCharacters( s );
        }

        serializeTo.addElement( "description" ).addCharacters( "descr" );

        return serializeTo;
      }

      @NotNull
      public List<String> deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws XMLStreamException, IOException {
        final List<String> strings = new ArrayList<String>();

        final boolean[] called = {false};

        visitChildren( deserializeFrom, new CB() {
          public void tagEntered( @NotNull XMLStreamReader2 deserializeFrom, @NotNull @NonNls String tagName ) throws XMLStreamException, IOException {
            if ( tagName.equals( "description" ) ) {
              assertEquals( getText( deserializeFrom ), "descr" );
              called[0] = true;
            } else {
              strings.add( getText( deserializeFrom ) );
            }
          }
        } );

        assertTrue( called[0] );

        return strings;
      }
    };
  }

  @NotNull
  @Override
  protected List<String> createObjectToSerialize() {
    return Arrays.asList( "1", "2", "3" );
  }

  @NotNull
  @Override
  protected String getExpectedSerializedString() {
    return "<aString><string>1</string><string>2</string><string>3</string><description>descr</description></aString>";
  }

  @Override
  protected void verifyDeserialized( @NotNull List<String> deserialized ) {
    assertEquals( deserialized, createObjectToSerialize() );
  }
}