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
package it.neckar.open.serialization.generator.intellij.stax.mate;

import it.neckar.open.serialization.generator.intellij.SerializerTestsGenerator;
import it.neckar.open.serialization.generator.intellij.model.FieldToSerialize;
import it.neckar.open.serialization.generator.intellij.model.SerializerModel;
import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.NullableNotNullManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.search.PsiShortNamesCache;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

/**
 * A simple class that generates a jackson serializer
 *
 */
public class StaxMateSerializerTestsGenerator implements SerializerTestsGenerator {
  @Nonnull
  private final Project project;
  @Nonnull
  private final CodeStyleManager codeStyleManager;
  @Nonnull
  private final JavaCodeStyleManager javaCodeStyleManager;
  @Nonnull
  private final PsiElementFactory elementFactory;
  @Nonnull
  private final JavaPsiFacade javaPsiFacade;
  @Nonnull
  private final PsiShortNamesCache shortNamesCache;
  @Nonnull
  private final NullableNotNullManager notNullManager;

  public StaxMateSerializerTestsGenerator( @Nonnull Project project ) {
    this.project = project;

    codeStyleManager = CodeStyleManager.getInstance( project );
    javaCodeStyleManager = JavaCodeStyleManager.getInstance( project );
    elementFactory = JavaPsiFacade.getElementFactory( project );
    javaPsiFacade = JavaPsiFacade.getInstance( project );
    shortNamesCache = PsiShortNamesCache.getInstance( project );
    notNullManager = NullableNotNullManager.getInstance( project );
  }

  @Nonnull
  public List<? extends PsiClass> generate( @Nonnull final SerializerModel serializerModel, @Nonnull final PsiDirectory testsTargetDir, @Nonnull final PsiDirectory testResourcesTargetDir ) {
    final PsiFile psiFile = serializerModel.getClassToSerialize().getContainingFile();

    //The directory the serializer is generated in
    final PsiClass[] testClasses = new PsiClass[2];

    new WriteCommandAction.Simple( serializerModel.getClassToSerialize().getProject(), psiFile ) {
      @Override
      protected void run() throws Throwable {
        testClasses[0] = JavaDirectoryService.getInstance().createClass( testsTargetDir, serializerModel.generateSerializerTestClassName() );
        testClasses[1] = JavaDirectoryService.getInstance().createClass( testsTargetDir, serializerModel.generateSerializerVersionTestClassName() );

        fillTest( serializerModel, testClasses[0] );
        fillVersionTest( serializerModel, testClasses[1] );


        //Now create the resource file
        testResourcesTargetDir.createFile( generateTestResourceName( serializerModel.getClassToSerialize().getName(), 1 ) );

        //Now beautify the code
        codeStyleManager.reformat( testClasses[0] );
        javaCodeStyleManager.shortenClassReferences( testClasses[0] );
        javaCodeStyleManager.optimizeImports( testClasses[0].getContainingFile() );
        codeStyleManager.reformat( testClasses[1] );
        javaCodeStyleManager.shortenClassReferences( testClasses[1] );
        javaCodeStyleManager.optimizeImports( testClasses[1].getContainingFile() );
      }
    }.execute();

    return ImmutableList.copyOf( testClasses );
  }

  private void fillVersionTest( @Nonnull SerializerModel serializerModel, @Nonnull PsiClass versionTestClass ) {
    PsiClass classToSerialize = serializerModel.getClassToSerialize();
    addExtends( versionTestClass, classToSerialize, "it.neckar.open.serialization.test.utils.AbstractXmlVersionTest2" );

    versionTestClass.add( generateGetSerializerMethod( serializerModel, versionTestClass ) );
    versionTestClass.add( generateVersionEntry( serializerModel, versionTestClass, 1 ) );

    versionTestClass.add( generateVerifyDeserialized( serializerModel, versionTestClass ) );
  }

  private PsiElement generateVerifyDeserialized( @Nonnull SerializerModel serializerModel, @Nonnull PsiClass versionTestClass ) {
    StringBuilder methodBuilder = new StringBuilder();

    methodBuilder.append( "@Override protected void verifyDeserialized(" )
      .append( notNull() )
      .append( serializerModel.getClassToSerialize().getQualifiedName() )
      .append( " deserialized, " )
      .append( notNull() ).append( " it.neckar.open.version.Version version" )
    ;

    methodBuilder.append( "){" );

    for ( FieldToSerialize entry : serializerModel.getFieldToSerializeEntries() ) {
      methodBuilder.append( "org.assertj.core.api.Assertions.assertThat(deserialized." ).append( entry.getAccessor() ).append( ").isNotNull();" );
    }

    methodBuilder.append( "}" );

    return elementFactory.createMethodFromText( methodBuilder.toString(), versionTestClass );
  }

  @Nonnull
  public PsiClass fillTest( @Nonnull SerializerModel serializerModel, @Nonnull PsiClass testClass ) {
    PsiClass classToSerialize = serializerModel.getClassToSerialize();
    addExtends( testClass, classToSerialize, "it.neckar.open.serialization.test.utils.AbstractXmlSerializerTest2" );

    testClass.add( generateGetSerializerMethod( serializerModel, testClass ) );
    testClass.add( generateEntry( serializerModel, testClass, 1 ) );


    return testClass;
  }

  @Nonnull
  private PsiField generateEntry( @Nonnull SerializerModel serializerModel, @Nonnull PsiClass testClass, int entryIndex ) {
    StringBuilder methodBuilder = new StringBuilder();

    methodBuilder.append( notNull() ).append( "@org.junit.experimental.theories.DataPoint public static final it.neckar.open.serialization.test.utils.Entry<? extends " )
      .append( serializerModel.getClassToSerialize().getQualifiedName() )
      .append( "> ENTRY" ).append( entryIndex )
      .append( "=" )
      .append( "it.neckar.open.serialization.test.utils.AbstractSerializerTest2" ).append( ".create(" )
    ;


    //TODO add object generation
    methodBuilder.append( "new " ).append( serializerModel.getClassToSerialize().getQualifiedName() ).append( "()" );
    methodBuilder.append( ", " ).append( testClass.getQualifiedName() ).append( ".class.getResource(\"" ).append( generateTestResourceName( serializerModel.getClassToSerialize().getName(), entryIndex ) ).append( "\"));" );

    return elementFactory.createFieldFromText( methodBuilder.toString(), testClass );
  }

  @Nonnull
  private PsiField generateVersionEntry( @Nonnull SerializerModel serializerModel, @Nonnull PsiClass testClass, int entryIndex ) {
    StringBuilder methodBuilder = new StringBuilder();

    methodBuilder.append( notNull() ).append( "@org.junit.experimental.theories.DataPoint public static final it.neckar.open.serialization.test.utils.VersionEntry ENTRY" ).append( entryIndex )
      .append( "=" )
      .append( "it.neckar.open.serialization.test.utils.AbstractXmlVersionTest2" ).append( ".create(it.neckar.open.version.Version.valueOf( 1, 0, 0 )" )
    ;

    methodBuilder.append( ", " ).append( testClass.getQualifiedName() ).append( ".class.getResource(\"" ).append( generateTestResourceName( serializerModel.getClassToSerialize().getName(), entryIndex ) ).append( "\"));" );

    return elementFactory.createFieldFromText( methodBuilder.toString(), testClass );
  }

  @Nonnull
  private static String generateTestResourceName( @Nonnull String qualifiedName, int entryIndex ) {
    return qualifiedName + "_1.0.0_" + entryIndex + ".xml";
  }

  private void addExtends( @Nonnull PsiClass serializerClass, @Nonnull PsiClass classToSerialize, final String baseClass ) {
    //Add extends abstract base class
    {
      PsiJavaCodeReferenceElement extendsRef = elementFactory.createReferenceFromText( baseClass + "<" + classToSerialize.getQualifiedName() + ">", classToSerialize );

      PsiReferenceList extendsList = serializerClass.getExtendsList();
      assert extendsList != null;
      extendsList.add( extendsRef );
    }
  }

  @Nonnull
  private PsiElement generateGetSerializerMethod( @Nonnull SerializerModel serializerModel, @Nonnull PsiClass testClass ) {
    StringBuilder methodBuilder = new StringBuilder();

    methodBuilder.append( notNull() ).append( "@Override\n" ).append( "  protected it.neckar.open.serialization.StreamSerializer<" ).append( serializerModel.getClassToSerialize().getQualifiedName() ).append( "> getSerializer() throws Exception {" );
    methodBuilder.append( "return com.google.inject.Guice.createInjector().getInstance(" ).append( serializerModel.generateSerializerClassName() ).append( ".class);" );
    methodBuilder.append( "}" );

    return elementFactory.createMethodFromText( methodBuilder.toString(), testClass );
  }

  private String notNull() {
    return "@" + notNullManager.getDefaultNotNull() + " ";
  }

  /**
   * Creates the json serializedType for the given class name
   *
   * @param className the class name
   * @return the json serializedType
   */
  @Nonnull
  private String createType( @Nonnull String className ) {
    return javaCodeStyleManager.suggestVariableName( VariableKind.STATIC_FINAL_FIELD, className, null, null ).names[0].toLowerCase( Locale.getDefault() );
  }
}
