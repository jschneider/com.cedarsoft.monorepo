package com.cedarsoft.codegen;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class JDirectInnerClassTest {
  private JCodeModel owner;

  @Test
  public void testName() throws Exception {
    JClass outerRef = new ClassRefSupport( owner ).ref( JDirectInnerClassTest.class );

    JDirectInnerClass inner = new JDirectInnerClass( owner, outerRef, "Foo" );

    assertNotNull( inner.outer() );
    assertEquals( "JDirectInnerClassTest", inner.outer().name() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest", inner.outer().fullName() );
    assertEquals( "Foo", inner.name() );

    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest.Foo", inner.fullName() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest$Foo", inner.binaryName() );
  }

  @Test
  public void testNotExisting() throws Exception {
    JClass inner = new ClassRefSupport( owner ).ref( "com.cedarsoft.codegen.JDirectInnerClassTest$Bar" );

    assertEquals( JDirectInnerClass.class, inner.getClass() );

    assertNotNull( inner.outer() );
    assertEquals( "JDirectInnerClassTest", inner.outer().name() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest", inner.outer().fullName() );
    assertEquals( "Bar", inner.name() );

    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest.Bar", inner.fullName() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest$Bar", ( ( JDirectInnerClass ) inner ).binaryName() );
  }

  @Before
  public void setUp() throws Exception {
    owner = new JCodeModel();
  }

  public static class Foo {

  }
}
