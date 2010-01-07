package com.cedarsoft.serialization.stax;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.serialization.AbstractXmlSerializerTest;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.testng.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

/**
 *
 */
public class StaxMateCollectionSerializerTest extends AbstractXmlSerializerTest<List<String>> {
  @NotNull
  @Override
  protected AbstractStaxMateSerializer<List<String>> getSerializer() {
    return new AbstractStaxMateSerializer<List<String>>( "aString", "http://aString", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) ) {
      @Override
      public void serialize( @NotNull SMOutputElement serializeTo, @NotNull List<String> object ) throws XMLStreamException {
        for ( String s : object ) {
          serializeTo.addElement( serializeTo.getNamespace(), "string" ).addCharacters( s );
        }

        serializeTo.addElement( serializeTo.getNamespace(), "description" ).addCharacters( "descr" );
      }

      @Override
      @NotNull
      public List<String> deserialize( @NotNull XMLStreamReader deserializeFrom, @NotNull Version formatVersion ) throws XMLStreamException, IOException {
        final List<String> strings = new ArrayList<String>();

        final boolean[] called = {false};

        visitChildren( deserializeFrom, new CB() {
          @Override
          public void tagEntered( @NotNull XMLStreamReader deserializeFrom, @NotNull @NonNls String tagName ) throws XMLStreamException, IOException {
            if ( tagName.equals( "description" ) ) {
              Assert.assertEquals( getText( deserializeFrom ), "descr" );
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
  protected String getExpectedSerialized() {
    return "<aString><string>1</string><string>2</string><string>3</string><description>descr</description></aString>";
  }
}