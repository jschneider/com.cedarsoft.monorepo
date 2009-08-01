package com.cedarsoft.utils.configuration.xml;

import com.jgoodies.binding.value.ValueModel;
import org.jetbrains.annotations.NotNull;

/**
 * Binds a configuration to a bean.
 */
public class ConfigurationBinding {
  private ConfigurationBinding() {
  }

  /**
   * Binds a value model to a configuration
   *
   * @param configurationAccess the configuration access
   * @param valueModel          the value model
   * @return the configuration connector
   */
  public static <T> ConfigurationConnector<T> bind( @NotNull ConfigurationAccess<T> configurationAccess, @NotNull ValueModel valueModel ) {
    ConfigurationConnector<T> connector = new ConfigurationConnector<T>( valueModel, configurationAccess );
    connector.readFromConfiguration();
    return connector;
  }
}
