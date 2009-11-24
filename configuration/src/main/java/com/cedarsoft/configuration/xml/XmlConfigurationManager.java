package com.cedarsoft.configuration.xml;

import com.google.inject.Inject;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;

/**
 * Configures configurations
 */
public class XmlConfigurationManager {
  @NonNls
  private static final String MODULES_KEY = "modules";

  @NotNull
  private final XMLConfiguration configuration;

  @Inject
  public XmlConfigurationManager( @NotNull XMLConfiguration configuration ) {
    this.configuration = configuration;

    if ( this.configuration.getFileName() != null ) {
      Runtime.getRuntime().addShutdownHook( new Thread( new Runnable() {
        @Override
        public void run() {
          try {
            XmlConfigurationManager.this.configuration.save();
          } catch ( ConfigurationException e ) {
            throw new RuntimeException( e );
          }
        }
      } ) );
    }
  }

  /**
   * Returns the configuration for the given moduleType
   *
   * @param moduleType the moduleType
   * @return the configuration for the given  moduleType
   */
  @NotNull
  public HierarchicalConfiguration getModuleConfiguration( @NotNull Class<?> moduleType ) {
    String moduleTypeName = moduleType.getName().replaceAll( "\\$", "." );
    HierarchicalConfiguration modulesConfiguration = getModulesConfiguration();
    try {
      return modulesConfiguration.configurationAt( moduleTypeName );
    } catch ( IllegalArgumentException ignore ) {
      modulesConfiguration.addProperty( moduleTypeName, "" );
      return modulesConfiguration.configurationAt( moduleTypeName );
    }
  }

  @NotNull
  HierarchicalConfiguration getModulesConfiguration() {
    try {
      return configuration.configurationAt( MODULES_KEY );
    } catch ( IllegalArgumentException ignore ) {
      configuration.addProperty( MODULES_KEY, "" );
      return configuration.configurationAt( MODULES_KEY );
    }
  }

  @NotNull
  public HierarchicalConfiguration getConfiguration() {
    return configuration;
  }
}
