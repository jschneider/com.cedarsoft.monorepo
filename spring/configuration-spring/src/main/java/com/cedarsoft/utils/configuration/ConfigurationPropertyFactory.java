package com.cedarsoft.utils.configuration;

import com.cedarsoft.utils.configuration.xml.ConfigurationAccess;
import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;

import java.lang.Override;

/**
 * This factory reads a value from the configuration manager
 */
@Deprecated
public class ConfigurationPropertyFactory<T> implements FactoryBean {
  @NotNull
  private final ConfigurationAccess<T> configurationAccess;

  public ConfigurationPropertyFactory( @NotNull Configuration configuration, @NotNull Class<T> type, @NotNull @NonNls String key, @NotNull DefaultValueProvider defaultValueProvider ) {
    this.configurationAccess = new ConfigurationAccess<T>( configuration, type, key, defaultValueProvider );
  }

  @Override
  @Nullable
  public Object getObject() throws Exception {
    return configurationAccess.resolve();
  }

  @Override
  @NotNull
  public Class<?> getObjectType() {
    return configurationAccess.getType();
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
