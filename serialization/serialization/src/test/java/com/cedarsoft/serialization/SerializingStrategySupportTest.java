package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.serialization.stax.StaxMateSerializingStrategy;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.annotations.*;

import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class SerializingStrategySupportTest {
  @Test
  public void testGenerics() {
    SerializingStrategySupport<String, Object, MyStrategy> support = new SerializingStrategySupport<String, Object, MyStrategy>( Arrays.asList( new MyStrategy() ) );
    SerializingStrategySupport<String, Object, StaxMateSerializingStrategy<String, Object>> support2 = new SerializingStrategySupport<String, Object, StaxMateSerializingStrategy<String, Object>>( Arrays.asList( new MyStrategy() ) );

    List<StaxMateSerializingStrategy<? extends String, Object>> strategies1 = Arrays.<StaxMateSerializingStrategy<? extends String, Object>>asList( new MyStrategy() );

    SerializingStrategySupport<String, Object, StaxMateSerializingStrategy<String, Object>> support5 = new SerializingStrategySupport<String, Object, StaxMateSerializingStrategy<String, Object>>( strategies1 );

    Collection<? extends StaxMateSerializingStrategy<? extends String, Object>> strategies = new ArrayList<StaxMateSerializingStrategy<? extends String, Object>>();
    SerializingStrategySupport<String, Object, StaxMateSerializingStrategy<String, Object>> support3 = new SerializingStrategySupport<String, Object, StaxMateSerializingStrategy<String, Object>>( strategies );
  }

  private static class MyStrategy implements StaxMateSerializingStrategy<String, Object> {
    @Override
    @NotNull
    public String getId() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void serialize( @NotNull String object, @NotNull OutputStream out, @Nullable Object context ) throws IOException {
      throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String deserialize( @NotNull InputStream in, @Nullable Object context ) throws IOException {
      throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Version getFormatVersion() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean supports( @NotNull Object object ) {
      throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull String object, @Nullable Object context ) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public String deserialize( @NotNull @NonNls XMLStreamReader deserializeFrom, Object context ) throws IOException {
      throw new UnsupportedOperationException();
    }
  }

}
