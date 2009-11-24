package com.cedarsoft.configuration;

import com.cedarsoft.configuration.xml.XmlConfigurationManager;
import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;

import java.lang.Override;

/**
 * <p/>
 * Date: Jul 1, 2007<br>
 * Time: 6:39:21 PM<br>
 */
@Deprecated
public class ConfigurationFactory implements FactoryBean {
  @NotNull
  private final XmlConfigurationManager configurationManager;
  @Nullable
  private final Class<?> moduleType;

  public ConfigurationFactory( @NotNull XmlConfigurationManager configurationManager ) {
    this.configurationManager = configurationManager;
    moduleType = null;
  }

  public ConfigurationFactory( @NotNull XmlConfigurationManager configurationManager, @Nullable Class<?> moduleType ) {
    this.configurationManager = configurationManager;
    this.moduleType = moduleType;
  }

  @Override
  @NotNull
  public Object getObject() throws Exception {
    if ( moduleType != null ) {
      return configurationManager.getModuleConfiguration( moduleType );
    } else {
      return configurationManager.getConfiguration();
    }
  }

  @Override
  @NotNull
  public Class<Configuration> getObjectType() {
    return Configuration.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
