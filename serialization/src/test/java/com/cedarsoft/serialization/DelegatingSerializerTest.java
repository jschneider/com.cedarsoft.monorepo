package com.cedarsoft.serialization;

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
public class DelegatingSerializerTest extends AbstractSerializerTest<Number>{
  @NotNull
  @Override
  protected AbstractSerializer<Number> getSerializer() {
    return new MySerializer( new AbstractSerializingStrategy<Number>( "int", Integer.class ) {
      public void serialize( @NotNull Element element, @NotNull Number object ) throws IOException {
        element.setText( object.toString() );
      }
      @NotNull
      public Number deserialize( @NotNull @NonNls Element element ) throws IOException {
        return 1;
      }
    }, new AbstractSerializingStrategy<Number>( "double", Double.class ) {
      public void serialize( @NotNull Element element, @NotNull Number object ) throws IOException {
        element.setText( object.toString() );
      }

      @NotNull
      public Number deserialize( @NotNull @NonNls Element element ) throws IOException {
        return 2.0;
      }
    } );
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
    MySerializer serializer = new MySerializer( new AbstractSerializingStrategy<Number>( "int", Integer.class ) {
      public void serialize( @NotNull Element element, @NotNull Number object ) throws IOException {
        element.setText( object.toString() );
      }
      @NotNull
      public Number deserialize( @NotNull @NonNls Element element ) throws IOException {
        return 1;
      }
    }, new AbstractSerializingStrategy<Number>( "double", Double.class ) {
      public void serialize( @NotNull Element element, @NotNull Number object ) throws IOException {
        element.setText( object.toString() );
      }

      @NotNull
      public Number deserialize( @NotNull @NonNls Element element ) throws IOException {
        return 2.0;
      }
    } );

    assertEquals( serializer.getStrategies().size(), 2 );

    assertEquals( new String( serializer.serialize( 1 ) ).trim(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"int\">1</number>" );
    assertEquals( new String( serializer.serialize( 2.0 ) ).trim(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<number type=\"double\">2.0</number>" );
  }


  public static class MySerializer extends AbstractDelegatingSerializer<Number> {
    public MySerializer( @NotNull SerializingStrategy<Number>... serializingStrategies ) {
      super( "number", serializingStrategies );
    }

    public MySerializer( @NotNull Collection<? extends SerializingStrategy<Number>> serializingStrategies ) {
      super( "number", serializingStrategies );
    }
  }
}
