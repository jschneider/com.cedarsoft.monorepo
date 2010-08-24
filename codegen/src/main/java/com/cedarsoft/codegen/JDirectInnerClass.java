package com.cedarsoft.codegen;

import com.google.common.base.Splitter;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JTypeVar;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class JDirectInnerClass extends JClass {
  @NotNull
  private final JClass outer;

  private final String name;

  public JDirectInnerClass( @NotNull JCodeModel owner, @NotNull JClass outer, @NotNull @NonNls String name ) {
    super( owner );
    this.outer = outer;
    this.name = name;
  }

  @Override
  public JClass outer() {
    return outer;
  }

  @Override
  public void generate( JFormatter f ) {
    super.generate( f );
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String fullName() {
    return outer.fullName() + "." + name;
  }

  public String binaryName() {
    return outer.fullName() + "$" + name;
  }

  @Override
  public JPackage _package() {
    return outer._package();
  }

  @Override
  public JClass _extends() {
    return owner().ref( Object.class );
  }

  @Override
  public Iterator<JClass> _implements() {
    return Collections.<JClass>emptyList().iterator();
  }

  @Override
  public boolean isInterface() {
    return false;
  }

  @Override
  public boolean isAbstract() {
    return false;
  }

  @Override
  protected JClass substituteParams( JTypeVar[] variables, List<JClass> bindings ) {
    return this;
  }
}
