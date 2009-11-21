package com.cedarsoft.serialization;

import com.cedarsoft.serialization.stax.StaxMateSerializingStrategy;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.Override;
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
    SerializingStrategySupport<String, MyStrategy> support = new SerializingStrategySupport<String, MyStrategy>( Arrays.asList( new MyStrategy() ) );
    SerializingStrategySupport<String, StaxMateSerializingStrategy<String>> support2 = new SerializingStrategySupport<String, StaxMateSerializingStrategy<String>>( Arrays.asList( new MyStrategy() ) );

    List<StaxMateSerializingStrategy<? extends String>> strategies1 = Arrays.<StaxMateSerializingStrategy<? extends String>>asList( new MyStrategy() );

    SerializingStrategySupport<String, StaxMateSerializingStrategy<String>> support5 = new SerializingStrategySupport<String, StaxMateSerializingStrategy<String>>( strategies1 );

    Collection<? extends StaxMateSerializingStrategy<? extends String>> strategies = new ArrayList<StaxMateSerializingStrategy<? extends String>>();
    SerializingStrategySupport<String, StaxMateSerializingStrategy<String>> support3 = new SerializingStrategySupport<String, StaxMateSerializingStrategy<String>>( strategies );
  }

  private static class MyStrategy implements StaxMateSerializingStrategy<String> {
    @Override
    @NotNull
    public String getId() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean supports( @NotNull Object object ) {
      throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull String object ) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public String deserialize( @NotNull @NonNls XMLStreamReader2 deserializeFrom ) throws IOException {
      throw new UnsupportedOperationException();
    }
  }

}
