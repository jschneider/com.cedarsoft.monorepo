package com.cedarsoft.utils.configuration.xml;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.jgoodies.binding.beans.BeanAdapter;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 */
public class ConfigBindingGuiceTest {
  private Injector injector;

  @BeforeMethod
  protected void setUp() throws Exception {

    injector = Guice.createInjector( new AbstractModule() {
      @Override
      protected void configure() {
        bind( Configuration.class ).toInstance( new BaseConfiguration() );

        bind( MyBean.class ).toProvider( new Provider<MyBean>() {
          @Inject
          Configuration configuration;

          @java.lang.Override
          @NotNull
          public MyBean get() {
            MyBean myBean = new MyBean();
            BeanAdapter<MyBean> beanAdapter = new BeanAdapter<MyBean>( myBean, true );
            ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, MyBean.PROPERTY_VALUE, "theDefaultValue" );
            ConfigurationBinding.bind( configurationAccess, beanAdapter.getValueModel( MyBean.PROPERTY_VALUE ) );
            return myBean;
          }
        } ).in( Scopes.SINGLETON );
      }
    } );
  }

  @Test
  public void testGetConnector() {
    assertEquals( "theDefaultValue", injector.getInstance( MyBean.class ).getValue() );
    injector.getInstance( MyBean.class ).setValue( "asdf" );
    assertEquals( "asdf", injector.getInstance( Configuration.class ).getString( MyBean.PROPERTY_VALUE ) );
  }

  public static class MyBean {
    @NotNull
    @NonNls
    static final String PROPERTY_VALUE = "value";

    private PropertyChangeSupport pcs = new PropertyChangeSupport( this );
    private String value;

    public String getValue() {
      return value;
    }

    public void setValue( String value ) {
      pcs.firePropertyChange( PROPERTY_VALUE, this.value, this.value = value );
    }

    public void addPropertyChangeListener( PropertyChangeListener listener ) {
      pcs.addPropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener ) {
      pcs.removePropertyChangeListener( listener );
    }

    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener ) {
      pcs.addPropertyChangeListener( propertyName, listener );
    }

    public void removePropertyChangeListener( String propertyName, PropertyChangeListener listener ) {
      pcs.removePropertyChangeListener( propertyName, listener );
    }
  }
}
