package com.cedarsoft.utils.configuration.xml;

import com.cedarsoft.utils.configuration.DefaultValueProvider;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.lang.Override;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Date: Jun 28, 2007<br>
 * Time: 4:04:13 PM<br>
 */
public class ConfigurationAccessTest {
  private Configuration configuration;

  @BeforeMethod
  protected void setUp() throws Exception {
    configuration = new BaseConfiguration();
  }

  @Test
  public void testMap() {
    Map<String, String> defaults = new HashMap<String, String>();
    defaults.put( "a", "ab" );
    defaults.put( "b", "bb" );
    ConfigurationAccess<Map> configurationAccess = new ConfigurationAccess<Map>( configuration, Map.class, "key", defaults );

    //Default Value
    assertEquals( 2, configurationAccess.resolve().size() );

    Map<String, String> newMap = new HashMap<String, String>();
    newMap.put( "c", "cc" );
    newMap.put( "d", "dd" );
    newMap.put( "e", "ee" );
    configurationAccess.store( newMap );

    assertEquals( 3, newMap.size() );
    assertEquals( "cc", configurationAccess.resolve().get( "c" ) );
    assertEquals( "dd", configurationAccess.resolve().get( "d" ) );
    assertEquals( "ee", configurationAccess.resolve().get( "e" ) );
  }

  @Test
  public void testWrite() {
    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "key", "defValue" );
    assertEquals( "defValue", configurationAccess.resolve() );
    assertEquals( "defValue", configuration.getString( "key" ) );

    configurationAccess.store( "newValue" );

    assertEquals( "newValue", configurationAccess.resolve() );
    assertEquals( "newValue", configuration.getString( "key" ) );
  }

  @Test
  public void testList() {
    assertEquals( 0, ( ( List<String> ) configuration.getList( "asdf" ) ).size() );

    configuration.addProperty( "asdf", Arrays.asList( "a", "b", "c" ) );
    assertEquals( 3, ( ( List<String> ) configuration.getList( "asdf" ) ).size() );

    ConfigurationAccess<List> configurationAccess = new ConfigurationAccess<List>( configuration, List.class, "asdf", new DefaultValueProvider() {
      @Override
      @NotNull
      public <T> T getDefaultValue( @NotNull @NonNls String key, @NotNull Class<T> type ) {
        throw new UnsupportedOperationException();
      }
    } );

    List<String> result = configurationAccess.resolve();
    assertEquals( 3, result.size() );
  }

  @Test
  public void testCallback() {
    DefaultValueProvider defaultValueProvider = new DefaultValueProvider() {
      @Override
      @NotNull
      public <T> T getDefaultValue( @NotNull @NonNls String key, @NotNull Class<T> type ) {
        return type.cast( "1234" );
      }
    };
    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "key", defaultValueProvider );
    assertEquals( "1234", configurationAccess.resolve() );

    assertEquals( "1234", configuration.getString( "key" ) );
  }

  @Test
  public void testStringResolver() {
    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "key", "defValue" );

    assertEquals( "defValue", configurationAccess.resolve() );
    configuration.setProperty( "key", "2" );
    assertEquals( "2", configurationAccess.resolve() );
    configuration.setProperty( "key", 2 );
    try {
      configurationAccess.resolve();
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }
  }

  @Test
  public void testIntResolver() {
    ConfigurationAccess<Integer> configurationAccess = new ConfigurationAccess<Integer>( configuration, Integer.class, "key", 5 );
    assertEquals( new Integer( 5 ), configurationAccess.resolve() );
    configuration.setProperty( "key", 2 );
    assertEquals( new Integer( 2 ), configurationAccess.resolve() );
  }
}
