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
package plugin.action;

import com.cedarsoft.serialization.generator.intellij.jackson.JacksonSerializerResolver;
import com.cedarsoft.serialization.generator.intellij.jackson.JacksonSerializerTestsGenerator;
import com.cedarsoft.serialization.generator.intellij.model.SerializerModel;
import com.cedarsoft.serialization.generator.intellij.model.SerializerModelFactory;
import com.google.common.collect.ImmutableList;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiField;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.refactoring.MultiFileTestCase;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 */
public class JacksonSerializerTestsGeneratorTest extends MultiFileTestCase {
  @Override
  protected Sdk getTestProjectJdk() {
    return new Jdk17MockProjectDescriptor().getSdk();
  }

  @Override
  protected String getTestDataPath() {
    return new File( "testData" ).getAbsolutePath();
  }

  @Override
  protected String getTestRoot() {
    String className = getClass().getName();

    int lastIndex = className.lastIndexOf( '.' );
    String relevantName = className.substring( lastIndex + 1 );

    return "/" + relevantName + "/";
  }

  @Test
  public void testSimple() throws Throwable {
    doTest( new PerformAction() {
      @Override
      public void performAction( VirtualFile rootDir, VirtualFile rootAfter ) throws Exception {
        PsiClass simple = myJavaFacade.findClass( getTestName( false ), GlobalSearchScope.allScope( getProject() ) );
        assertThat( simple ).isNotNull();
        assertThat( simple.getQualifiedName() ).isEqualTo( getTestName( false ) );

        SerializerModelFactory serializerModelFactory = new SerializerModelFactory(new JacksonSerializerResolver( getProject() ), JavaCodeStyleManager.getInstance( getProject() ), JavaPsiFacade.getInstance(getProject()));
        SerializerModel model = serializerModelFactory.create( simple, ImmutableList.of( simple.findFieldByName( "foo", false ) ) );


        PsiDirectory dir = simple.getContainingFile().getContainingDirectory();

        JacksonSerializerTestsGenerator generator = new JacksonSerializerTestsGenerator( getProject() );
        List<? extends PsiClass> tests = generator.generate( model, dir, dir );
        assertThat( tests ).hasSize( 2 );
        assertThat( tests.get( 0 ).getName() ).isEqualTo( "SimpleSerializerTest" );
      }
    } );
  }

  @Test
  public void testSetter() throws Throwable {
    doTest( new PerformAction() {
      @Override
      public void performAction( VirtualFile rootDir, VirtualFile rootAfter ) throws Exception {
        PsiClass simple = myJavaFacade.findClass( getTestName( false ), GlobalSearchScope.allScope( getProject() ) );
        assertThat( simple ).isNotNull();
        assertThat( simple.getQualifiedName() ).isEqualTo( getTestName( false ) );

        SerializerModelFactory serializerModelFactory = new SerializerModelFactory(new JacksonSerializerResolver( getProject() ), JavaCodeStyleManager.getInstance( getProject() ), JavaPsiFacade.getInstance(getProject()));
        SerializerModel model = serializerModelFactory.create( simple, ImmutableList.of( simple.findFieldByName( "foo", false ) ) );

        PsiDirectory dir = simple.getContainingFile().getContainingDirectory();

        JacksonSerializerTestsGenerator generator = new JacksonSerializerTestsGenerator( getProject() );
        List<? extends PsiClass> tests = generator.generate( model, dir, dir );
        assertThat( tests ).hasSize( 2 );
        assertThat( tests.get( 0 ).getName() ).isEqualTo( "SetterSerializerTest" );
      }
    } );
  }

  @Test
  public void testPrimitives() throws Throwable {
    doTest( new PerformAction() {
      @Override
      public void performAction( VirtualFile rootDir, VirtualFile rootAfter ) throws Exception {
        PsiClass foo = myJavaFacade.findClass( getTestName( false ), GlobalSearchScope.allScope( getProject() ) );
        assertThat( foo ).isNotNull();
        assertThat( foo.getQualifiedName() ).isEqualTo( getTestName( false ) );

        SerializerModelFactory serializerModelFactory = new SerializerModelFactory(new JacksonSerializerResolver( getProject() ), JavaCodeStyleManager.getInstance( getProject() ), JavaPsiFacade.getInstance(getProject()));
        SerializerModel model = serializerModelFactory.create( foo, ImmutableList.<PsiField>copyOf( foo.getAllFields() ) );


        PsiDirectory dir = foo.getContainingFile().getContainingDirectory();

        JacksonSerializerTestsGenerator generator = new JacksonSerializerTestsGenerator( getProject() );

        List<? extends PsiClass> tests = generator.generate( model, dir, dir );
        assertThat( tests ).hasSize( 2 );
        assertThat( tests.get( 0 ).getName() ).isEqualTo( "PrimitivesSerializerTest" );
      }
    } );
  }
}
