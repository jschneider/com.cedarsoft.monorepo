package com.cedarsoft.utils.configuration.xml;

import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves a value from a configuration.
 * This class offers a common access to a configuration for every type.
 */
public abstract class ConfigurationResolver<T> {
  /**
   * Resulve the value of the configuration for the given key
   *
   * @param configuration the configuration the value is extracted for
   * @param key           the key
   * @return the value
   */
  @Nullable
  public abstract T resolve( @NotNull Configuration configuration, @NotNull @NonNls String key );

  /**
   * Stores the value to the configuration
   *
   * @param configuration the configuration
   * @param key           the key
   * @param value         the value
   */
  public abstract void store( @NotNull Configuration configuration, @NotNull @NonNls String key, @NotNull T value );

  /**
   * Returns the resolver for the given type.
   *
   * @param type the type
   * @return the resolver for the given type
   */
  @NotNull
  public static <T> ConfigurationResolver<T> getResolver( @NotNull Class<? extends T> type ) {
    ConfigurationResolver<?> resolver = resolvers.get( type );
    if ( resolver == null ) {
      throw new IllegalArgumentException( "No resolver found for type: " + type );
    }
    //noinspection unchecked
    return ( ConfigurationResolver<T> ) resolver;
  }

  @NotNull
  private static final Map<Class<?>, ConfigurationResolver<?>> resolvers = new HashMap<Class<?>, ConfigurationResolver<?>>();

  static {
    resolvers.put( String.class, new StringResolver() );
    resolvers.put( Double.class, new DoubleResolver() );
    resolvers.put( Integer.class, new IntegerResolver() );
    resolvers.put( File.class, new FileResolver() );
    resolvers.put( List.class, new ListResolver() );
    resolvers.put( Map.class, new MapResolver() );
  }

  public static class MapResolver extends ConfigurationResolver<Map<?, ?>> {
    @Override
    @Nullable
    public Map<?, ?> resolve( @NotNull Configuration configuration, @NotNull @NonNls String key ) {
      List<String> list = configuration.getList( key );
      if ( list.isEmpty() ) {
        return null;
      }

      Map<String, String> values = new HashMap<String, String>();
      for ( String raw : list ) {
        String[] parts = raw.split( "\\|" );
        if ( parts.length != 2 ) {
          throw new IllegalStateException( "Invalid Entry: \"" + raw + '\"' );
        }

        values.put( parts[0], parts[1] );
      }

      return values;
    }

    @Override
    public void store( @NotNull Configuration configuration, @NotNull @NonNls String key, @NotNull Map<?, ?> value ) {
      List<String> condenced = new ArrayList<String>();
      for ( Map.Entry<?, ?> entry : value.entrySet() ) {
        condenced.add( entry.getKey() + "|" + entry.getValue() );
      }
      configuration.setProperty( key, condenced );
    }
  }

  public static class ListResolver extends ConfigurationResolver<List<?>> {
    @Override
    @Nullable
    public List<?> resolve( @NotNull Configuration configuration, @NotNull @NonNls String key ) {
      List<?> list = configuration.getList( key );
      if ( list.isEmpty() ) {
        return null;
      }
      return list;
    }

    @Override
    public void store( @NotNull Configuration configuration, @NotNull @NonNls String key, @NotNull List<?> value ) {
      configuration.setProperty( key, value );
    }
  }

  public static class StringResolver extends ConfigurationResolver<String> {
    @Override
    @Nullable
    public String resolve( @NotNull Configuration configuration, @NotNull @NonNls String key ) {
      return configuration.getString( key );
    }

    @Override
    public void store( @NotNull Configuration configuration, @NotNull @NonNls String key, @NotNull String value ) {
      configuration.setProperty( key, value );
    }
  }

  public static class FileResolver extends ConfigurationResolver<File> {
    @Override
    @Nullable
    public File resolve( @NotNull Configuration configuration, @NotNull @NonNls String key ) {
      String path = configuration.getString( key );
      return path == null ? null : new File( path );
    }

    @Override
    public void store( @NotNull Configuration configuration, @NotNull @NonNls String key, @NotNull File value ) {
      configuration.setProperty( key, value.getAbsolutePath() );
    }
  }

  private static class DoubleResolver extends ConfigurationResolver<Double> {
    @Override
    @Nullable
    public Double resolve( @NotNull Configuration configuration, @NotNull @NonNls String key ) {
      return configuration.getDouble( key );
    }

    @Override
    public void store( @NotNull Configuration configuration, @NotNull @NonNls String key, @NotNull Double value ) {
      configuration.setProperty( key, value );
    }
  }

  private static class IntegerResolver extends ConfigurationResolver<Integer> {
    @Override
    @Nullable
    public Integer resolve( @NotNull Configuration configuration, @NotNull @NonNls String key ) {
      return configuration.getInt( key );
    }

    @Override
    public void store( @NotNull Configuration configuration, @NotNull @NonNls String key, @NotNull Integer value ) {
      configuration.setProperty( key, value );
    }
  }
}
