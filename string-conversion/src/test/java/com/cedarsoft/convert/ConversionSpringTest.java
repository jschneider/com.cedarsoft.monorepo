package com.cedarsoft.convert;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: 23.01.2007<br>
 * Time: 11:26:52<br>
 */
public class ConversionSpringTest {
  @Test
  public void testIt() {
    ClassPathResource resource = new ClassPathResource( "/com/cedarsoft/convert/converter.spr.xml" );

    BeanFactory factory = new XmlBeanFactory( resource );
    StringConverterManager manager = ( StringConverterManager ) factory.getBean( "defaultStringConverterManager", StringConverterManager.class );
    assertNotNull( manager );
    assertEquals( 6, manager.getConverterMap().size() );

    assertEquals( "java.lang.String", manager.serialize( String.class ) );
  }

  @Test
  public void testOverride() {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext( new String[]{
      "/com/cedarsoft/convert/converter.spr.xml",
      "/com/cedarsoft/convert/converter_test.spr.xml"
    } );
    StringConverterManager manager = ( StringConverterManager ) applicationContext.getBean( "customStringConverterManager", StringConverterManager.class );
    assertNotNull( manager );
    assertEquals( 7, manager.getConverterMap().size() );

    assertEquals( "faafdfsd", manager.serialize( new StringBuilder( "faafdfsd" ) ) );
  }

  public static class AnObjectConverter implements StringConverter<StringBuilder> {
    @Override
    @NotNull
    public String createRepresentation( @NotNull StringBuilder object ) {
      return object.toString();
    }

    @Override
    @NotNull
    public StringBuilder createObject( @NotNull String representation ) {
      return new StringBuilder( representation );
    }
  }

}
