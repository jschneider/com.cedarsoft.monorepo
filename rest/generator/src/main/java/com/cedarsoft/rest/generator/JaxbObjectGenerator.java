package com.cedarsoft.rest.generator;

import com.cedarsoft.codegen.AbstractGenerator;
import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.DecisionCallback;
import com.cedarsoft.codegen.GeneratorConfiguration;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.DomainObjectDescriptorFactory;
import com.cedarsoft.codegen.parser.Parser;
import com.cedarsoft.codegen.parser.Result;
import org.jetbrains.annotations.NotNull;

/**
 * Generates a Jaxb object corresponding to a domain object
 */
public class JaxbObjectGenerator extends AbstractGenerator {
  public static void main( String[] args ) throws Exception {
    new JaxbObjectGenerator().run( args );
  }

  @NotNull
  @Override
  protected String getRunnerClassName() {
    return "com.cedarsoft.rest.generator.JaxbObjectGenerator$MyRunner";
  }

  public static class MyRunner implements Runner {
    @Override
    public void generate( @NotNull GeneratorConfiguration configuration ) throws Exception {
      Result result = Parser.parse( configuration.getDomainSourceFile() );

      CodeGenerator<MyDecisionCallback> codeGenerator = new CodeGenerator<MyDecisionCallback>( new MyDecisionCallback() );

      DomainObjectDescriptor descriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration() ).create();
      new Generator( codeGenerator, descriptor ).generate();

      codeGenerator.getModel().build( configuration.getDestination(), System.out );
    }
  }

  public static class MyDecisionCallback implements DecisionCallback {

  }

}
