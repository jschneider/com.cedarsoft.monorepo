package com.cedarsoft.utils.configuration.xml;

import com.cedarsoft.utils.configuration.DefaultValueProvider;
import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

/**
 * Offers common access to configurations.
 * Implementations are independant of the type.
 */
public class ConfigurationAccess<T> {
  @NotNull
  private final Class<? extends T> type;
  @NotNull
  @NonNls
  private final String key;
  @NotNull
  private final ConfigurationResolver<T> resolver;
  @NotNull
  private final DefaultValueProvider defaultValueProvider;
  @NotNull
  private final Configuration configuration;

  public ConfigurationAccess( @NotNull Configuration configuration, @NotNull Class<? extends T> type, @NotNull @NonNls String key, @NotNull final T defaultValue ) {
    this( configuration, type, key, new DefaultValueProvider() {
      @NotNull
      public <T> T getDefaultValue( @NotNull @NonNls String key, @NotNull Class<T> type ) {
        return type.cast( defaultValue );
      }
    } );
  }

  public ConfigurationAccess( @NotNull Configuration configuration, @NotNull Class<? extends T> type, @NotNull @NonNls String key, @NotNull DefaultValueProvider defaultValueProvider ) {
    this( configuration, type, key, defaultValueProvider, ConfigurationResolver.getResolver( type ) );
  }

  public ConfigurationAccess( @NotNull Configuration configuration, @NotNull Class<? extends T> type, @NotNull @NonNls String key, @NotNull DefaultValueProvider defaultValueProvider, @NotNull ConfigurationResolver<T> resolver ) {
    this.configuration = configuration;
    this.type = type;
    this.key = key;
    this.defaultValueProvider = defaultValueProvider;
    this.resolver = resolver;
  }

  @Nullable
  public T resolve() {
    try {
      T resolved = resolver.resolve( configuration, key );
      if ( resolved != null ) {
        return resolved;
      }
    } catch ( NoSuchElementException ignore ) {
    }
    T defaultValue = defaultValueProvider.getDefaultValue( key, type );
    if ( defaultValue != null ) {
      resolver.store( configuration, key, defaultValue );
    }
    return defaultValue;
  }

  /**
   * Resolves the value
   *
   * @return the resolved value
   *
   * @throws IllegalArgumentException if the key is invalid
   */
  @NotNull
  public T resolveSafe() throws IllegalArgumentException {
    T resolved = resolve();
    if ( resolved == null ) {
      throw new IllegalArgumentException( "No configured value found for " + key );
    }
    return resolved;
  }

  @NotNull
  public Class<? extends T> getType() {
    return type;
  }

  @NotNull
  @NonNls
  public String getKey() {
    return key;
  }

  public void store( @NotNull T value ) {
    resolver.store( configuration, key, value );
  }
}
