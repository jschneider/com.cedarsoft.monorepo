package com.cedarsoft.utils.configuration;

import com.cedarsoft.utils.configuration.ConfigurationManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation for configuration manager
 */
public class DefaultConfigurationManager implements ConfigurationManager {
  @NotNull
  private final List<Object> configurations = new ArrayList<Object>();

  public DefaultConfigurationManager() {
  }

  public DefaultConfigurationManager(@NotNull  List<?> initialConfigurations ) {
    configurations.addAll( initialConfigurations );
  }

  public void addConfiguration( @NotNull Object configuration ) {
    configurations.add( configuration );
  }

  @NotNull
  public List<?> getConfigurations() {
    return Collections.unmodifiableList( configurations );
  }

  @NotNull
  public <T> T getConfiguration( @NotNull Class<T> configurationType ) throws IllegalArgumentException {
    for ( Object configuration : configurations ) {
      if ( configuration.getClass().equals( configurationType ) ) {
        return configurationType.cast( configuration );
      }
    }

    throw new IllegalArgumentException( "No configuration found of type " + configurationType.getName() );
  }
}

