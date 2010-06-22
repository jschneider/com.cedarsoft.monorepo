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
  @Test
  public void testGeneratorTest() throws URISyntaxException, JClassAlreadyExistsException, IOException {
    URL resource = getClass().getResource( "test/BarModel.java" );
    Result result = Parser.parse( new File( resource.toURI() ) );

    DomainObjectDescriptor descriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration() ).create();

    CodeGenerator<JaxbObjectGenerator.MyDecisionCallback> codeGenerator = new CodeGenerator<JaxbObjectGenerator.MyDecisionCallback>( new JaxbObjectGenerator.MyDecisionCallback() );
    new Generator( codeGenerator, descriptor ).generate();


    ByteArrayOutputStream out = new ByteArrayOutputStream();
    codeGenerator.getModel().build( new SingleStreamCodeWriter( out ) );

    AssertUtils.assertEquals( out.toString(), getClass().getResource( "JaxbObjectGeneratorTest.1.txt" ) );
  }

  @Test
  public void testPac() {
    assertEquals( Generator.insertSubPackage( "a.b.c.d.E", "ins" ), "a.b.c.d.ins.E" );
    assertEquals( Generator.insertSubPackage( "a.b.c.d.E", "e" ), "a.b.c.d.e.E" );
  }
}
