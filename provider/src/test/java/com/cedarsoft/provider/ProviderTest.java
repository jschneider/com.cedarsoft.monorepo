package com.cedarsoft.provider;

import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.io.IOException;

import static org.testng.Assert.fail;

/**
 *
 */
public class ProviderTest {
  @Test
  public void testException() {
    Provider<String, IOException> provider = new Provider<String, IOException>() {
      @java.lang.Override
      @NotNull
      public String provide() throws IOException {
        throw new IOException( "Uuups" );
      }

      @java.lang.Override
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
