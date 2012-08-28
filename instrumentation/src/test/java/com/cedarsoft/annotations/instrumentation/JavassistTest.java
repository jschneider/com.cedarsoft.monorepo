package com.cedarsoft.annotations.instrumentation;

import static org.fest.assertions.Assertions.assertThat;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.annotation.Nonnull;

import org.fest.reflect.core.Reflection;
import org.junit.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JavassistTest {
  @Test
  public void testIt() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass ctClass = pool.get("com.cedarsoft.annotations.instrumentation.MyTestClass");
    assertThat(ctClass).isNotNull();

    CtMethod[] methods = ctClass.getMethods();
    assertThat(methods).hasSize(13);

    CtMethod method = findFirstMethod(ctClass.getMethods(), "atest");
    assertThat(method).isNotNull();
    method.insertBefore("System.out.println(\"changed by javassist\");");
    method.insertBefore("System.out.println(\"changed by javassist2\");");

    StringBuilder builder = new StringBuilder();
    builder
      .append("if (")
      .append(method.getDeclaringClass().getName())
      .append(".class.desiredAssertionStatus()")
      .append("){")
      .append("throw new AssertionError((Object)\"Hey, it worked\");")
      .append("}")
    ;

    method.insertBefore(builder.toString());

    assertThat(ctClass.toBytecode()).isNotNull();

    URL resource = getClass().getResource("/com/cedarsoft/annotations/instrumentation/MyTestClass.class");
    assertThat(resource).isNotNull();
    File file = new File(resource.toURI());

    DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
    try {
      ctClass.toBytecode(out);
    } finally {
      out.close();
    }

    Class<?> type = Reflection.type("com.cedarsoft.annotations.instrumentation.MyTestClass").load();
    Object instance = Reflection.constructor().in(type).newInstance();
    Reflection.method("atest").in(instance).invoke();
  }

  private static CtMethod findFirstMethod(@Nonnull CtMethod[] methods, @Nonnull String name) {
    for (CtMethod method : methods) {
      if (method.getName().equals(name)) {
        return method;
      }
    }
    throw new IllegalArgumentException("no method found for " + name + " in " + Arrays.toString(methods));
  }
}
