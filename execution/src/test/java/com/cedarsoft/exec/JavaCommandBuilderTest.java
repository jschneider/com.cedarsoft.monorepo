package com.cedarsoft.exec;

import org.testng.annotations.*;

import java.io.File;
import java.util.List;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: Jun 18, 2007<br>
 * Time: 1:49:47 PM<br>
 */
public class JavaCommandBuilderTest {
  private JavaCommandBuilder starter;

  @BeforeMethod
  protected void setUp() throws Exception {
    starter = new JavaCommandBuilder( "mainClass" );
  }

  @Test
  public void testClasspath() {
    String[] classPathElements = new String[]{"a", "b", "c"};
    starter.setClassPathElements( classPathElements );
    assertEquals( 3, starter.getClassPathElements().size() );

    assertEquals( "a" + File.pathSeparator + "b" + File.pathSeparator + "c", starter.getClassPath() );

    starter.addClassPathElement( "d" );
    assertEquals( "a" + File.pathSeparator + "b" + File.pathSeparator + "c" + File.pathSeparator + "d", starter.getClassPath() );
  }

  @Test
  public void testVmProperties() {
    starter.setVmProperties( "key=value", "key2=value2" );

    assertEquals( 2, starter.getVmProperties().size() );
    assertEquals( "key=value", starter.getVmProperties().get( 0 ) );
    assertEquals( "key2=value2", starter.getVmProperties().get( 1 ) );

    starter.addVmProperty( "d=e" );
    assertEquals( "d=e", starter.getVmProperties().get( 2 ) );
  }

  @Test
  public void testArguments() {
    starter.setArguments( "a", "b", "c" );
    assertEquals( 3, starter.getArguments().size() );
  }

  @Test
  public void testAll() {
    assertEquals( "java mainClass", starter.getCommandLine() );

    starter.setClassPathElements( "a", "b" );
    assertEquals( "java -cp a" + File.pathSeparator + "b mainClass", starter.getCommandLine() );

    starter.setArguments( "arg", "arg1" );
    assertEquals( "java -cp a" + File.pathSeparator + "b mainClass arg arg1", starter.getCommandLine() );

    starter.setVmProperties( "prop0", "prop1" );
    assertEquals( "java -Dprop0 -Dprop1 -cp a" + File.pathSeparator + "b mainClass arg arg1", starter.getCommandLine() );


    List<String> elements = starter.getCommandLineElements();
    assertEquals( 8, elements.size() );
    assertEquals( "java", elements.get( 0 ) );
    assertEquals( "-Dprop0", elements.get( 1 ) );
    assertEquals( "-Dprop1", elements.get( 2 ) );
    assertEquals( "-cp", elements.get( 3 ) );
    assertEquals( "a" + File.pathSeparator + "b", elements.get( 4 ) );
    assertEquals( "mainClass", elements.get( 5 ) );
    assertEquals( "arg", elements.get( 6 ) );
    assertEquals( "arg1", elements.get( 7 ) );
  }
}
