package com.cedarsoft.codegen;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JFormatter;
import org.fest.reflect.core.Reflection;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JAnnotationFieldReference extends JAnnotationValue {
  @NonNls
  public static final String FIELD_OWNER = "owner";
  @NonNls
  public static final String METHOD_ADD_VALUE = "addValue";
  @NotNull
  private final JFieldVar ns;
  @NotNull
  private final JDefinedClass owner;

  public JAnnotationFieldReference( @NotNull JFieldVar ns ) {
    this.ns = ns;
    this.owner = Reflection.field( FIELD_OWNER ).ofType( JDefinedClass.class ).in( ns ).get();
  }

  @Override
  public void generate( JFormatter f ) {
    f.p( owner.name() );
    f.p( "." );
    f.p( ns.name() );
  }

  @NotNull
  public static JAnnotationUse param( @NotNull JAnnotationUse annotation, @NotNull @NonNls String key, @NotNull JFieldVar ref ) {
    Reflection.method( METHOD_ADD_VALUE ).withParameterTypes( String.class, JAnnotationValue.class )
      .in( annotation ).invoke( key, new JAnnotationFieldReference( ref ) );
    return annotation;
  }
}
