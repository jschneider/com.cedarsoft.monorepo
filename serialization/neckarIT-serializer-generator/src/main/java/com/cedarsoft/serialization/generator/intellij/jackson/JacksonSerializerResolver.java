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
package it.neckar.open.serialization.generator.intellij.jackson;

import it.neckar.open.serialization.generator.intellij.SerializerResolver;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.util.Processor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Resolver for serializers for a given type.
 *
 */
public class JacksonSerializerResolver implements SerializerResolver {
  @Nonnull
  public static final String SERIALIZER_IFACE_NAME = "it.neckar.open.serialization.jackson.JacksonSerializer";

  @Nonnull
  private final JavaPsiFacade javaPsiFacade;
  @Nonnull
  private final PsiElementFactory elementFactory;

  @Nonnull
  private final Project project;

  public JacksonSerializerResolver( @Nonnull Project project ) {
    this.project = project;
    javaPsiFacade = JavaPsiFacade.getInstance( project );
    elementFactory = JavaPsiFacade.getElementFactory( project );
  }

  /**
   * Returns the serializer for the given serializedType
   *
   * @param typeToSerialize the serializedType that shall be serialized
   * @return the found jackson serializer
   */
  @Override
  @Nonnull
  public PsiType findSerializerFor( @Nonnull PsiType typeToSerialize ) {
    //Fix scope: GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(  )
    PsiClass serializerClass = javaPsiFacade.findClass( SERIALIZER_IFACE_NAME, GlobalSearchScope.allScope( project ) );
    if (serializerClass == null) {
      //Fallback if there is no jackson serializer interface
      return elementFactory.createTypeByFQClassName(guessSerializerName(typeToSerialize));
    }

    final PsiType unBoxedTypeToSerialize;
    if (typeToSerialize instanceof PsiPrimitiveType) {
      unBoxedTypeToSerialize = ((PsiPrimitiveType) typeToSerialize).getBoxedType(serializerClass);
      if (unBoxedTypeToSerialize == null) {
        throw new IllegalStateException("No boxed type found for <" + serializerClass + ">");
      }
    }else{
      unBoxedTypeToSerialize = typeToSerialize;
    }

    String genericsType = unBoxedTypeToSerialize.getCanonicalText();

    final PsiType jacksonSerializerWithTypeParam = elementFactory.createTypeFromText(SERIALIZER_IFACE_NAME + "<" + genericsType + ">", null);

    final PsiType[] foundSerializerType = new PsiType[1];
    ClassInheritorsSearch.search(serializerClass).forEach(new Processor<PsiClass>() {
      @Override
      public boolean process(PsiClass psiClass) {
        //Skip interfaces and abstract classes
        if (psiClass.isInterface() || psiClass.hasModifierProperty(PsiModifier.ABSTRACT)) {
          return true;
        }

        //Is it a serializer?
        PsiClassType currentSerializerType = elementFactory.createType(psiClass);
        if (!jacksonSerializerWithTypeParam.isAssignableFrom(currentSerializerType)) {
          return true;
        }

        //Verify the exact type param
        PsiClassType jacksonSerializerImplType = findJacksonSerializerImplFor(currentSerializerType);
        if (jacksonSerializerImplType == null) {
          return true;
        }

        PsiType[] parameters = jacksonSerializerImplType.getParameters();
        if (parameters.length != 1) {
          return true;
        }

        PsiType parameter = parameters[0];
        if (!parameter.equals(unBoxedTypeToSerialize)) {
          return true;
        }

        foundSerializerType[0] = currentSerializerType;
        return false;
      }

      @Nullable
      private PsiClassType findJacksonSerializerImplFor(@Nonnull PsiClassType serializerType) {
        for (PsiType superType : serializerType.getSuperTypes()) {
          PsiClass psiClass = ((PsiClassType) superType).resolve();
          assert psiClass != null;
          String qualifiedName = psiClass.getQualifiedName();

          if (SERIALIZER_IFACE_NAME.equals(qualifiedName)) {
            return (PsiClassType) superType;
          }

          @Nullable PsiClassType oneDown = findJacksonSerializerImplFor((PsiClassType) superType);
          if (oneDown != null) {
            return oneDown;
          }
        }

        return null;
      }
    });

    if (foundSerializerType[0] != null) {
      return foundSerializerType[0];
    }

    //Fallback: Create a new pseudo serializer class
    return elementFactory.createTypeByFQClassName( guessSerializerName( typeToSerialize ) );
  }


  @Override
  @Nonnull
  public String guessSerializerName( @Nonnull PsiType typeToSerialize ) {
    if ( typeToSerialize instanceof PsiPrimitiveType ) {
      PsiClassType boxedType = ( ( PsiPrimitiveType ) typeToSerialize ).getBoxedType( PsiManager.getInstance( project ), GlobalSearchScope.allScope( project ) );
      if ( boxedType == null ) {
        throw new IllegalStateException( "No boxed type found for <" + typeToSerialize + ">" );
      }
      return boxedType.getPresentableText() + "Serializer";
    }

    return typeToSerialize.getPresentableText() + "Serializer";
  }
}
