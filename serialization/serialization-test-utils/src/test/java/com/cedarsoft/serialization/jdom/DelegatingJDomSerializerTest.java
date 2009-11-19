package com.cedarsoft.serialization.jdom;

import com.cedarsoft.AssertUtils;
import com.cedarsoft.Version;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Collection;

import static org.testng.Assert.*;

/**
 *
 */
public class DelegatingJDomSerializerTest extends AbstractJDomSerializerTest<Number> {
  private MySerializer serializer;

  @BeforeMethod
  protected void setUp() throws Exception {
    AbstractJDomSerializingStrategy<Integer> intSerializer = new AbstractJDomSerializingStrategy<Integer>( "int", Integer.class, new Version( 1, 0, 0 ) ) {
      @Override
      @NotNull
      public Element serialize( @NotNull Element element, @NotNull Integer object ) throws IOException {
        element.setText( object.toString() );
        return element;
      }

      @Override
      @NotNull
      public Integer deserialize( @NotNull @NonNls Element element ) throws IOException {
        return 1;
      }
    };
    AbstractJDomSerializingStrategy<Double> doubleSerializer = new AbstractJDomSerializingStrategy<Double>( "double", Double.class, new Version( 1, 0, 0 ) ) {
      @Override
      @NotNull
      public Element serialize( @NotNull Element element, @NotNull Double object ) throws IOException {
        element.setText( object.toString() );
        return element;
      }

      @Override
      @NotNull
      public Double deserialize( @NotNull @NonNls Element element ) throws IOException {
        return 2.0;
      }
    };
    serializer = new MySerializer( intSerializer, doubleSerializer );
  }

  @NotNull
  @Override
  protected AbstractJDomSerializer<Number> getSerializer() {
    return serializer;
  }

  @NotNull
  @Override
  protected Number createObjectToSerialize() {
    return 1;
  }

  @NotNull
  @Override
  protected String getExpectedSerializedString() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"int\">1</number>";
  }

  @Override
  protected void verifyDeserialized( @NotNull Number deserialized ) {
    assertEquals( 1, deserialized );
  }

  @Test
  public void tetIt() throws IOException, SAXException {
    assertEquals( serializer.getStrategies().size(), 2 );

    AssertUtils.assertXMLEqual( new String( serializer.serialize( 1 ) ).trim(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"int\">1</number>" );
    AssertUtils.assertXMLEqual( new String( serializer.serialize( 2.0 ) ).trim(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"double\">2.0</number>" );
  }

  public static class MySerializer extends AbstractDelegatingJDomSerializer<Number> {
    public MySerializer( @NotNull JDomSerializingStrategy<? extends Number>... serializingStrategies ) {
      super( "number", new Version( 1, 0, 0 ), serializingStrategies );
    }

    public MySerializer( @NotNull Collection<? extends JDomSerializingStrategy<? extends Number>> serializingStrategies ) {
      super( "number", new Version( 1, 0, 0 ), serializingStrategies );
    }
  }
}
