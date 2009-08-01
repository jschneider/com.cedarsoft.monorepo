package com.cedarsoft.utils.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Configuration manager that offers configurations for several modules
 */
public interface ConfigurationManager {
  /**
   * Returns the configuration of the given type
   *
   * @param configurationType the configuration type
   * @return the configuration
   */
  @NotNull
      <T> T getConfiguration( @NotNull Class<T> configurationType );

  /**
   * Adds a configuration
   *
   * @param configuration the configuration
   */
  void addConfiguration( @NotNull Object configuration );

  /**
   * Returns all configurations
   *
   * @return the configurations
   */
  @NotNull
  List<?> getConfigurations();
}
