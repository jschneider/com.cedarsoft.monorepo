package com.cedarsoft.configuration;

import com.cedarsoft.configuration.xml.ConfigurationAccess;
import com.cedarsoft.configuration.xml.ConfigurationConnector;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p/>
 * Date: Jul 5, 2007<br>
 * Time: 1:30:53 PM<br>
 */
@Deprecated
public class ConfigurationConnectorFactory<T> implements FactoryBean {
  @NotNull
  private final ConfigurationAccess<T> configurationAccess;
  private ValueModel valueModel;
  private final ConfigurationConnector<T> connector;

  public ConfigurationConnectorFactory( @NotNull Configuration configuration, @NotNull Class<T> type, @NotNull String propertyName, @NotNull DefaultValueProvider defaultValueProvider, @NotNull BeanAdapter<?> beanAdapter ) {
    valueModel = beanAdapter.getValueModel( propertyName );
    configurationAccess = new ConfigurationAccess<T>( configuration, type, propertyName, defaultValueProvider );
    connector = new ConfigurationConnector<T>( valueModel, configurationAccess );
    connector.readFromConfiguration();
  }

  public ConfigurationConnectorFactory( @NotNull Configuration configuration, @NotNull Class<T> type, @NotNull String propertyName, @NotNull T defaultValue, @NotNull BeanAdapter<?> beanAdapter ) {
    valueModel = beanAdapter.getValueModel( propertyName );
    configurationAccess = new ConfigurationAccess<T>( configuration, type, propertyName, defaultValue );
    connector = new ConfigurationConnector<T>( valueModel, configurationAccess );
    connector.readFromConfiguration();
  }

  public ConfigurationConnectorFactory( @NotNull ConfigurationAccess<T> configurationAccess, @NotNull BeanAdapter<?> beanAdapter ) {
    valueModel = beanAdapter.getValueModel( configurationAccess.getKey() );
    this.configurationAccess = configurationAccess;
    connector = new ConfigurationConnector<T>( valueModel, this.configurationAccess );
    connector.readFromConfiguration();
  }

  @Override
  public Object getObject() throws Exception {
    return connector;
  }

  @Override
  public Class<ConfigurationConnector> getObjectType() {
    return ConfigurationConnector.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
