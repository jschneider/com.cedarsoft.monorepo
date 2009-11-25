package com.cedarsoft.configuration.xml;

import com.jgoodies.binding.value.ValueModel;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * You don't have to use this class directly. Instead use
 * {@link ConfigurationBinding#bind(ConfigurationAccess,ValueModel)
 * <p/>
 * Connects a value model to a configuration access.
 * For each property of a bean one ConfigurationConnector is needed.
 * <p/>
 * T: The type of the property
 */
public class ConfigurationConnector<T> {
  @NotNull
  private final ValueModel subject;
  @NotNull
  private final ConfigurationAccess<T> configurationAccess;

  /**
   * Creates a new ConfigurationConnector
   *
   * @param subject             the value model of the bean
   * @param configurationAccess the configuration access
   */
  public ConfigurationConnector( @NotNull ValueModel subject, @NotNull ConfigurationAccess<T> configurationAccess ) {
    this.subject = subject;
    this.configurationAccess = configurationAccess;
    this.subject.addValueChangeListener( new SubjectChangeListener() );
  }

  /**
   * Returns the value model
   *
   * @return the value model
   */
  @NotNull
  public ValueModel getSubject() {
    return subject;
  }

  /**
   * Returns the configuration access
   *
   * @return the configuration access
   */
  @NotNull
  public ConfigurationAccess<T> getConfigurationAccess() {
    return configurationAccess;
  }

  /**
   * Reads from the configuration
   */
  public void readFromConfiguration() {
    subject.setValue( configurationAccess.resolve() );
  }

  private class SubjectChangeListener implements PropertyChangeListener {
    @Override
    public void propertyChange( PropertyChangeEvent evt ) {
      T value = configurationAccess.getType().cast( evt.getNewValue() );
      configurationAccess.store( value );
    }
  }
}
