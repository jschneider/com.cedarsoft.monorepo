package com.cedarsoft.utils.configuration.xml;

import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p/>
 * Date: Jul 2, 2007<br>
 * Time: 5:26:34 PM<br>
 */
public class ConfigurationBindingTest {
  private Configuration configuration;

  @BeforeMethod
  protected void setUp() throws Exception {
    configuration = new BaseConfiguration();
  }

  @Test
  public void testMapping() {
    MyBean bean = new MyBean();
    BeanAdapter<MyBean> beanAdapter = new BeanAdapter<MyBean>( bean, true );
    ValueModel valueModel = beanAdapter.getValueModel( "value" );

    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "valueKey", "theDefaultValue" );

    //manually
    ConfigurationConnector<String> connector = new ConfigurationConnector<String>( valueModel, configurationAccess );
    assertEquals( "", bean.getValue() );
    connector.readFromConfiguration();
    assertEquals( "theDefaultValue", bean.getValue() );
    assertEquals( "theDefaultValue", configuration.getString( "valueKey" ) );

    bean.setValue( "newValue" );
    assertEquals( "newValue", bean.getValue() );
    assertEquals( "newValue", configuration.getString( "valueKey" ) );
  }

  @Test
  public void testMapping2() {
    MyBean bean = new MyBean();
    BeanAdapter<MyBean> beanAdapter = new BeanAdapter<MyBean>( bean, true );

    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "valueKey", "theDefaultValue" );

    //manually
    ConfigurationBinding.bind( configurationAccess, beanAdapter.getValueModel( "value" ) );
    assertEquals( "theDefaultValue", bean.getValue() );
    assertEquals( "theDefaultValue", configuration.getString( "valueKey" ) );

    bean.setValue( "newValue" );
    assertEquals( "newValue", bean.getValue() );
    assertEquals( "newValue", configuration.getString( "valueKey" ) );
  }

  @Test
  public void testMappingList() {
    MyBean bean = new MyBean();
    assertEquals( 0, bean.getValues().size() );
    BeanAdapter<MyBean> beanAdapter = new BeanAdapter<MyBean>( bean, true );

    ConfigurationAccess<List> configurationAccess = new ConfigurationAccess<List>( configuration, List.class, "values", Arrays.asList( "a", "b" ) );
    ConfigurationBinding.bind( configurationAccess, beanAdapter.getValueModel( "values" ) );

    assertEquals( 2, bean.getValues().size() );
    assertEquals( "a", bean.getValues().get( 0 ) );
    assertEquals( "b", bean.getValues().get( 1 ) );

    assertEquals( 2, configuration.getList( "values" ).size() );
    assertEquals( "a", configuration.getList( "values" ).get( 0 ) );
    assertEquals( "b", configuration.getList( "values" ).get( 1 ) );

    bean.addValue( "c" );

    assertEquals( 3, bean.getValues().size() );
    assertEquals( "a", bean.getValues().get( 0 ) );
    assertEquals( "b", bean.getValues().get( 1 ) );
    assertEquals( "c", bean.getValues().get( 2 ) );

    assertEquals( 3, configuration.getList( "values" ).size() );
    assertEquals( "a", configuration.getList( "values" ).get( 0 ) );
    assertEquals( "b", configuration.getList( "values" ).get( 1 ) );
    assertEquals( "c", configuration.getList( "values" ).get( 2 ) );

    bean.removeValue( "b" );

    assertEquals( 2, bean.getValues().size() );
    assertEquals( "a", bean.getValues().get( 0 ) );
    assertEquals( "c", bean.getValues().get( 1 ) );

    assertEquals( 2, configuration.getList( "values" ).size() );
    assertEquals( "a", configuration.getList( "values" ).get( 0 ) );
    assertEquals( "c", configuration.getList( "values" ).get( 1 ) );
  }

  public static class MyBean {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );
    @NotNull
    @NonNls
    private String value = "";
    private final List<String> values = new ArrayList<String>();

    @NotNull
    public List<String> getValues() {
      return Collections.unmodifiableList( values );
    }

    public void setValues( @NotNull List<String> values ) {
      List<String> old = new ArrayList<String>( values );
      this.values.clear();
      this.values.addAll( values );
      pcs.firePropertyChange( "values", old, Collections.unmodifiableList( this.values ) );
    }

    public void addValue( @NotNull String otherValue ) {
      List<String> old = new ArrayList<String>( values );
      this.values.add( otherValue );
      pcs.firePropertyChange( "values", old, Collections.unmodifiableList( this.values ) );
    }

    public void removeValue( @NotNull String otherValue ) {
      List<String> old = new ArrayList<String>( values );
      this.values.remove( otherValue );
      pcs.firePropertyChange( "values", old, Collections.unmodifiableList( this.values ) );
    }

    @NotNull
    public String getValue() {
      return value;
    }

    public void setValue( @NotNull String value ) {
      pcs.firePropertyChange( "value", this.value, this.value = value );
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
