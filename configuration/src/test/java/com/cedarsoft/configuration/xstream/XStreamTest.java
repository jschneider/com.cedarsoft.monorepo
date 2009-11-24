package com.cedarsoft.configuration.xstream;

import com.cedarsoft.configuration.DefaultConfigurationManager;
import org.apache.commons.io.FileUtils;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 *
 */
public class XStreamTest {
  private DefaultConfigurationManager manager;
  private XStreamPersister persister;

  @BeforeMethod
  protected void setUp() throws Exception {
    manager = new DefaultConfigurationManager();
    persister = new XStreamPersister();
  }

  @Test
  public void testEncoding() {
    assertEquals( "<?xml version=\"1.0\"?>", persister.createXmlHeader( null ) );
    assertEquals( "<?xml version=\"1.0\" encoding=\"ENCODING\"?>", persister.createXmlHeader( "ENCODING" ) );
  }

  @Test
  public void testWrite() throws IOException {
    manager.addConfiguration( new MyModuleConfiguration( "asdf" ) );

    final StringWriter out = new StringWriter();

    persister.persist( manager, out );
    assertEquals( "<?xml version=\"1.0\"?>\n" +
        "<configurations>\n" +
        "  <com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
        "    <name>asdf</name>\n" +
        "  </com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
        "</configurations>", out.toString() );
  }

  @Test
  public void testFile() throws IOException {
    manager.addConfiguration( new MyModuleConfiguration( "asdf" ) );

    File file = File.createTempFile( "devtoolsConfig", ".xml" );
    persister.persist( manager, file );

    assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<configurations>\n" +
        "  <com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
        "    <name>asdf</name>\n" +
        "  </com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
        "</configurations>", FileUtils.readFileToString( file ) );


    DefaultConfigurationManager deserialized = new DefaultConfigurationManager( persister.load( file ) );
    assertEquals( 1, deserialized.getConfigurations().size() );
  }

  @Test
  public void testManager() {
    try {
      manager.getConfiguration( MyModuleConfiguration.class );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    manager.addConfiguration( new MyModuleConfiguration( "asdf" ) );

    assertNotNull( manager.getConfiguration( MyModuleConfiguration.class ) );
    assertSame( manager.getConfiguration( MyModuleConfiguration.class ), manager.getConfiguration( MyModuleConfiguration.class ) );
  }

  @Test
  public void testIt() throws IOException {
    assertEquals( "<?xml version=\"1.0\"?>\n" +
        "<configurations/>", persister.persist( manager ) );
  }

  @Test
  public void testNotSoSimple() throws IOException {
    manager.addConfiguration( new MyModuleConfiguration( "asdf" ) );
    assertNotNull( manager.getConfiguration( MyModuleConfiguration.class ) );

    String serialized = persister.persist( manager );
    assertEquals( "<?xml version=\"1.0\"?>\n" +
        "<configurations>\n" +
        "  <com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
        "    <name>asdf</name>\n" +
        "  </com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
        "</configurations>", serialized );

    DefaultConfigurationManager deserialized = new DefaultConfigurationManager( persister.load( serialized ) );
    assertNotNull( deserialized );
    assertNotNull( deserialized.getConfiguration( MyModuleConfiguration.class ) );

  }

  @Test
  public void testOtherConfig() throws IOException {
    manager.addConfiguration( new MyModuleConfiguration( "theName" ) );

    String serialized = persister.persist( manager );
    assertEquals( "<?xml version=\"1.0\"?>\n" +
        "<configurations>\n" +
        "  <com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
        "    <name>theName</name>\n" +
        "  </com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
        "</configurations>", serialized );

    DefaultConfigurationManager deserialized = new DefaultConfigurationManager( persister.load( serialized ) );
    assertNotNull( deserialized );
    assertNotNull( deserialized.getConfiguration( MyModuleConfiguration.class ) );
    assertEquals( "theName", deserialized.getConfiguration( MyModuleConfiguration.class ).getName() );
  }

  private static class MyModuleConfiguration {
    private String name;

    private MyModuleConfiguration( String name ) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
