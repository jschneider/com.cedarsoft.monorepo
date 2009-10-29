package com.cedarsoft.serialization.jdom;

import com.cedarsoft.serialization.SerializingStrategy;
import com.cedarsoft.serialization.jdom.AbstractDelegatingJDomSerializer;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializerTest;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializer;
import com.cedarsoft.serialization.jdom.AbstractJDomSerializingStrategy;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Collection;

/**
 *
 */
public class DelegatingJDomSerializerTest extends AbstractJDomSerializerTest<Number> {
  private MySerializer serializer;

  @BeforeMethod
  protected void setUp() throws Exception {
    AbstractJDomSerializingStrategy<Integer> intSerializer = new AbstractJDomSerializingStrategy<Integer>( "int", Integer.class ) {
      public void serialize( @NotNull Element element, @NotNull Integer object ) throws IOException {
        element.setText( object.toString() );
      }

      @NotNull
      public Integer deserialize( @NotNull @NonNls Element element ) throws IOException {
        return 1;
      }
    };
    AbstractJDomSerializingStrategy<Double> doubleSerializer = new AbstractJDomSerializingStrategy<Double>( "double", Double.class ) {
      public void serialize( @NotNull Element element, @NotNull Double object ) throws IOException {
        element.setText( object.toString() );
      }

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
  public void tetIt() throws IOException {
    assertEquals( serializer.getStrategies().size(), 2 );

    assertEquals( new String( serializer.serialize( 1 ) ).trim(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"int\">1</number>" );
    assertEquals( new String( serializer.serialize( 2.0 ) ).trim(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"double\">2.0</number>" );
  }


  public static class MySerializer extends AbstractDelegatingJDomSerializer<Number> {
    public MySerializer( @NotNull SerializingStrategy<? extends Number>... serializingStrategies ) {
      super( "number", serializingStrategies );
    }

    public MySerializer( @NotNull Collection<? extends SerializingStrategy<? extends Number>> serializingStrategies ) {
      super( "number", serializingStrategies );
    }
  }
}
