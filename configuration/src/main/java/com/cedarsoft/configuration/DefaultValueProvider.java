package com.cedarsoft.configuration;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the default value for a configuration. Sometimes it is usefull to provide
 * a hard coded default value. But sometimes it is necessary to check the system or ask
 * the user for the value.
 */
public interface DefaultValueProvider {
  /**
   * Provides the default value.
   * This method is called if no value is stored within the configuration
   *
   * @param key  the key
   * @param type the type
   * @return the default value
   */
  @NotNull
  <T> T getDefaultValue( @NotNull @NonNls String key, @NotNull Class<T> type );
}