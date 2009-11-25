package com.cedarsoft.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.util.Types;
import org.testng.annotations.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

/**
 *
 */
public class GuiceHelperTest {
  @Test
  public void testBindCollections() {
    AbstractModule module = new AbstractModule() {
      @Override
      protected void configure() {
        bind( ( ( Key<Set<String>> ) Key.get( Types.setOf( String.class ) ) ) ).toInstance( new HashSet<String>() );
        GuiceHelper.bindWildcardCollectionForSet( binder(), String.class );
      }
    };

    Guice.createInjector( module );
  }

  @Test
  public void testIt() {
    assertEquals( GuiceHelper.superCollectionOf( String.class ).toString(), "java.util.Collection<? extends java.lang.String>" );
    assertEquals( GuiceHelper.superListOf( String.class ).toString(), "java.util.List<? extends java.lang.String>" );
  }

  @Test
  public void testGuice() {
    assertEquals( Types.listOf( String.class ).toString(), "java.util.List<java.lang.String>" );
    assertEquals( Types.listOf( Types.subtypeOf( String.class ) ).toString(), "java.util.List<? extends java.lang.String>" );
    assertEquals( Types.newParameterizedType( Collection.class, Types.subtypeOf( String.class ) ).toString(), "java.util.Collection<? extends java.lang.String>" );
  }
}
