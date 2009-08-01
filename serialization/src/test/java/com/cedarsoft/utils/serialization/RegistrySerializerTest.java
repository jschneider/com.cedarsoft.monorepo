package com.cedarsoft.utils.serialization;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.utils.Registry;
import com.cedarsoft.utils.StillContainedException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class RegistrySerializerTest {
  private RegistrySerializer<String, Registry<String>> serializer;
  private RegistrySerializer.SerializedObjectsAccess access;

  @BeforeMethod
  public void setup() {
    access = new InMemorySerializedObjectsAccess();
    serializer = new RegistrySerializer<String, Registry<String>>( access, new AbstractSerializer<String>( "text" ) {
      @Override
      public void serialize( @NotNull Element element, @NotNull String object, @NotNull Lookup context ) {
        element.setText( object );
      }

      @Override
      @NotNull
      public String deserialize( @NotNull Element element, @NotNull Lookup context ) {
        return element.getTextNormalize();
      }
    }, new RegistrySerializer.IdResolver<String>() {
      @NotNull
      public String getId( @NotNull String object ) {
        return object;
      }
    } );
  }

  @Test
  public void testDuplicates() throws IOException {
    Registry<String> registry = serializer.createConnectedRegistry( new MyRegistryFactory() );
    assertEquals( registry.getStoredObjects().size(), 0 );
    registry.store( "asdf" );
    assertEquals( registry.getStoredObjects().size(), 1 );
    try {
      registry.store( "asdf" );
      fail( "Where is the Exception" );
    } catch ( StillContainedException ignore ) {
    }
    assertEquals( registry.getStoredObjects().size(), 1 );
  }

  @Test
  public void testDeserialize() throws IOException {
    serializer.add( "1" );

    assertEquals( serializer.deserialize().size(), 1 );
    assertEquals( serializer.deserialize().get( 0 ), "1" );
  }

  @Test
  public void testConnected() throws IOException {
    serializer.add( "1" );

    Registry<String> registry = serializer.createConnectedRegistry( new MyRegistryFactory() );
    assertEquals( registry.getStoredObjects().size(), 1 );
    assertEquals( registry.getStoredObjects().get( 0 ), "1" );

    registry.store( "2" );
    assertEquals( registry.getStoredObjects().size(), 2 );

    assertEquals( access.getStoredIds().size(), 2 );
  }

  @Test
  public void testEmptyConstrucot() throws IOException {
    Registry<String> registry = new Registry<String>( serializer.deserialize() );
    assertEquals( registry.getStoredObjects().size(), 0 );
  }

  @Test
  public void testMulti() throws IOException {
    assertEquals( access.getStoredIds().size(), 0 );

    serializer.add( "1" );

    assertEquals( access.getStoredIds().size(), 1 );
    try {
      serializer.add( "1" );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    Set<? extends String> ids = access.getStoredIds();
    assertEquals( ids.size(), 1 );
    assertTrue( ids.contains( "1" ) );

    assertEquals( serializer.getSerializer().deserialize( access.getInputStream( "1" ) ), "1" );
  }

  private static class MyRegistryFactory implements RegistrySerializer.RegistryFactory<String, Registry<String>> {
    @NotNull
    public Registry<String> createRegistry( @NotNull List<? extends String> objects, @NotNull Comparator<String> comparator ) {
      return new Registry<String>( objects, comparator );
    }
  }
}


