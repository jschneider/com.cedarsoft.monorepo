package com.cedarsoft.serialization;

import com.cedarsoft.AssertUtils;
import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.stax.AbstractDelegatingStaxMateSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializerTest;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializingStrategy;
import com.cedarsoft.serialization.stax.StaxMateSerializingStrategy;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.*;
import org.testng.annotations.*;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.util.Collection;

import static org.testng.Assert.*;

/**
 *
 */
public class DelegatingStaxMateSerializerTest extends AbstractStaxMateSerializerTest<Number, Object> {
  private MySerializer serializer;

  @BeforeMethod
  protected void setUp() throws Exception {
    AbstractStaxMateSerializingStrategy<Integer, Lookup> intSerializer = new AbstractStaxMateSerializingStrategy<Integer, Lookup>( "int", Integer.class, new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) ) {
      @Override
      @NotNull
      public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Integer object, @Nullable Lookup context ) throws IOException, XMLStreamException {
        serializeTo.addCharacters( object.toString() );
        return serializeTo;
      }

      @Override
      @NotNull
      public Integer deserialize( @NotNull XMLStreamReader deserializeFrom, @Nullable Lookup context ) throws IOException, XMLStreamException {
        getText( deserializeFrom );
        return 1;
      }
    };

    AbstractStaxMateSerializingStrategy<Double, Lookup> doubleSerializer = new AbstractStaxMateSerializingStrategy<Double, Lookup>( "double", Double.class, new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) ) {
      @Override
      @NotNull
      public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull Double object, @Nullable Lookup context ) throws IOException, XMLStreamException {
        serializeTo.addCharacters( object.toString() );
        return serializeTo;
      }

      @Override
      @NotNull
      public Double deserialize( @NotNull XMLStreamReader deserializeFrom, @Nullable Lookup context ) throws IOException, XMLStreamException {
        getText( deserializeFrom );
        return 2.0;
      }
    };
    serializer = new MySerializer( intSerializer, doubleSerializer );
  }

  @NotNull
  @Override
  protected AbstractStaxMateSerializer<Number, Object> getSerializer() {
    return serializer;
  }

  @NotNull
  @Override
  protected Number createObjectToSerialize() {
    return 1;
  }

  @NotNull
  @Override
  protected String getExpectedSerialized() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"int\">1</number>";
  }

  @Override
  protected void verifyDeserialized( @NotNull Number deserialized ) {
    assertEquals( 1, deserialized );
  }

  @Test
  public void tetIt() throws IOException, SAXException {
    Assert.assertEquals( serializer.getStrategies().size(), 2 );

    AssertUtils.assertXMLEqual( new String( serializer.serialize( 1 ) ).trim(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"int\">1</number>" );
    AssertUtils.assertXMLEqual( new String( serializer.serialize( 2.0 ) ).trim(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"double\">2.0</number>" );
  }


  public static class MySerializer extends AbstractDelegatingStaxMateSerializer<Number, Object> {
    public MySerializer( @NotNull StaxMateSerializingStrategy<? extends Number>... serializingStrategies ) {
      super( "number", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ), serializingStrategies );
    }

    public MySerializer( @NotNull Collection<? extends StaxMateSerializingStrategy<? extends Number>> serializingStrategies ) {
      super( "number", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ), serializingStrategies );
    }
  }
}
