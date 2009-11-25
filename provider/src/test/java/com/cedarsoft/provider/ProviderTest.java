package com.cedarsoft.provider;

import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.io.IOException;

import static org.testng.Assert.*;

/**
 *
 */
public class ProviderTest {
  @Test
  public void testException() {
    Provider<String, IOException> provider = new Provider<String, IOException>() {
      @Override
      @NotNull
      public String provide() throws IOException {
        throw new IOException( "Uuups" );
      }

      @Override
      @NotNull
      public String getDescription() {
        return "asdf";
      }
    };

    try {
      provider.provide();
      fail( "Where is the Exception" );
    } catch ( IOException ignore ) {
    }
  }
}
