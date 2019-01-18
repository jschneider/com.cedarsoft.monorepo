/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.annotations.instrumentation;

import static org.assertj.core.api.Assertions.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;

import javax.annotation.Nonnull;

import org.fest.reflect.core.Reflection;
import org.junit.jupiter.api.*;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.DuplicateMemberException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Disabled
public class JavassistTest {
  @Test
  public void testIt() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass ctClass = pool.get("com.cedarsoft.annotations.instrumentation.MyTestClass");
    assertThat(ctClass).isNotNull();

    try {
      ctClass.addField(CtField.make("static final boolean $assertionsDisabled = !" + ctClass.getName() + ".class.desiredAssertionStatus();", ctClass));
      System.out.println("--> no duplicate");
    } catch (DuplicateMemberException ignore) {
      System.out.println("--> Duplicate");
    }

    CtMethod[] methods = ctClass.getMethods();
    assertThat(methods).hasSize(13);

    CtMethod method = findFirstMethod(ctClass.getMethods(), "atest");
    assertThat(method).isNotNull();
    method.insertBefore("System.out.println(\"changed by javassist\");");
    method.insertBefore("System.out.println(\"changed by javassist2\");");

    StringBuilder builder = new StringBuilder();
    builder
      .append("if ( !$assertionsDisabled )")
      .append("{")
      .append("throw new AssertionError((Object)\"Hey, it worked\");")
      .append("}")
    .append("else{System.out.println(123);}")
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
