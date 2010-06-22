package com.cedarsoft.rest.generator;

import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.DecisionCallback;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class AbstractGenerator<T extends DecisionCallback> {
  @NotNull
  protected final CodeGenerator<T> codeGenerator;
  @NotNull
  protected final DomainObjectDescriptor descriptor;

  public AbstractGenerator( @NotNull CodeGenerator<T> codeGenerator, @NotNull DomainObjectDescriptor descriptor ) {
    this.codeGenerator = codeGenerator;
    this.descriptor = descriptor;
  }

  @NotNull
  @NonNls
  protected String getJaxbClassName() {
    String fqn = descriptor.getQualifiedName();
    return insertSubPackage( fqn, "jaxb" );
  }

  @NotNull
  @NonNls
  public static String insertSubPackage( @NotNull @NonNls String fqn, @NotNull @NonNls String packagePart ) {
    int lastIndex = fqn.lastIndexOf( "." );
    return fqn.substring( 0, lastIndex ) + "." + packagePart + fqn.substring( lastIndex );
  }
}
