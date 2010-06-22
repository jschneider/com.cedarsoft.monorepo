package com.cedarsoft.rest.generator;

import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.NamingSupport;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.FieldWithInitializationInfo;
import com.cedarsoft.id.NameSpaceSupport;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
public class Generator extends AbstractGenerator {

  public Generator( @NotNull CodeGenerator<JaxbObjectGenerator.MyDecisionCallback> codeGenerator, @NotNull DomainObjectDescriptor descriptor ) {
    super( codeGenerator, descriptor );
  }

  public void generate() throws JClassAlreadyExistsException {
    JDefinedClass jaxbClass = codeGenerator.getModel()._class( getJaxbClassName() );
    jaxbClass.annotate( XmlRootElement.class ).param( "namespace", NameSpaceSupport.createNameSpaceUriBase( descriptor.getQualifiedName() ) );

    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldsToSerialize() ) {
      JClass fieldType = codeGenerator.ref( fieldInfo.getType() );
      JFieldVar field = addField( jaxbClass, fieldType, fieldInfo );
      addGetter( jaxbClass, fieldType, fieldInfo, field );
      addSetter( jaxbClass, fieldType, fieldInfo, field );
    }
  }

  private void addSetter( @NotNull JDefinedClass jaxbClass, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo, @NotNull JFieldVar field ) {
    JMethod setter = jaxbClass.method( JMod.PUBLIC, Void.TYPE, NamingSupport.createSetter( fieldInfo.getSimpleName() ) );
    JVar param = setter.param( fieldType, fieldInfo.getSimpleName() );
    setter.body().assign( JExpr._this().ref( field ), param );
  }

  private void addGetter( @NotNull JDefinedClass jaxbClass, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo, @NotNull JFieldVar field ) {
    JMethod getter = jaxbClass.method( JMod.PUBLIC, fieldType, NamingSupport.createGetterName( fieldInfo.getSimpleName() ) );
    getter.body()._return( field );
  }

  @NotNull
  private JFieldVar addField( @NotNull JDefinedClass jaxbClass, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo ) {
    return jaxbClass.field( JMod.PRIVATE, fieldType, fieldInfo.getSimpleName() );
  }

}
