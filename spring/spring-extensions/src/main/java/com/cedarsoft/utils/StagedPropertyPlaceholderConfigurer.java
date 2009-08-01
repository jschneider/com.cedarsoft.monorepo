package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;
import java.util.Properties;

/**
 * Staged property placeholder configurer.
 * The system property {@link #STAGE_KEY} is used as prefix for the given properties file.
 * If the property is not set, the default value "dev" is used.
 */
public class StagedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
  @NotNull
  @NonNls
  public static final String STAGE_KEY = "stage";

  @NonNls
  private String actualStage;

  @NotNull
  @NonNls
  public String getActualStage() {
    if ( actualStage == null ) {
      actualStage = System.getProperty( STAGE_KEY );
      if ( actualStage == null || actualStage.length() == 0 || actualStage.equals( "null" ) ) {
        actualStage = "dev"; //Setting the default value (for unit testing etc).
      }
    }
    return actualStage;
  }

  public void setActualStage( @NotNull @NonNls String actualStage ) {
    this.actualStage = actualStage;
  }

  @Override
  protected void processProperties( @NotNull ConfigurableListableBeanFactory beanFactoryToProcess, @NotNull Properties props ) throws BeansException {
    super.processProperties( beanFactoryToProcess, filter( props ) );
  }

  @NotNull
  private Properties filter( @NotNull Properties props ) {
    String stage = getActualStage();

    Properties result = new Properties();
    for ( Map.Entry<Object, Object> entry : props.entrySet() ) {
      String key = String.valueOf( entry.getKey() );
      if ( key.startsWith( stage ) ) {
        String newKey = key.substring( stage.length() + 1 );
        result.setProperty( newKey, ( String ) entry.getValue() );
      }
    }
    return result;
  }
}
