package com.cedarsoft.rest.generator;

import com.cedarsoft.AssertUtils;
import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.DomainObjectDescriptorFactory;
import com.cedarsoft.codegen.parser.Parser;
import com.cedarsoft.codegen.parser.Result;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.writer.SingleStreamCodeWriter;
import org.testng.annotations.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.testng.Assert.*;

/**
 *
 */
public class JaxbObjectGeneratorTest {
  private URL resource;
  private Result result;
  private DomainObjectDescriptor descriptor;
  private CodeGenerator<JaxbObjectGenerator.MyDecisionCallback> codeGenerator;

  @BeforeMethod
  protected void setUp() throws Exception {
    resource = getClass().getResource( "test/BarModel.java" );
    result = Parser.parse( new File( resource.toURI() ) );
    descriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration() ).create();
    codeGenerator = new CodeGenerator<JaxbObjectGenerator.MyDecisionCallback>( new JaxbObjectGenerator.MyDecisionCallback() );
  }

  @Test
  public void testGeneratModel() throws URISyntaxException, JClassAlreadyExistsException, IOException {
    new Generator( codeGenerator, descriptor ).generate();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    codeGenerator.getModel().build( new SingleStreamCodeWriter( out ) );

    AssertUtils.assertEquals( out.toString(), getClass().getResource( "JaxbObjectGeneratorTest.1.txt" ) );
  }

  @Test
  public void testGeneratTest() throws Exception {
    new TestGenerator( codeGenerator, descriptor ).generateTest();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    codeGenerator.getModel().build( new SingleStreamCodeWriter( out ) );

    AssertUtils.assertEquals( out.toString(), getClass().getResource( "JaxbObjectGeneratorTest.test.txt" ) );
  }

  @Test
  public void testPac() {
    assertEquals( Generator.insertSubPackage( "a.b.c.d.E", "ins" ), "a.b.c.d.ins.E" );
    assertEquals( Generator.insertSubPackage( "a.b.c.d.E", "e" ), "a.b.c.d.e.E" );
  }
}
