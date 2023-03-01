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
package it.neckar.open.serialization.generator.intellij.model;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PropertyUtil;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 */
public class FieldAccessProvider {
  @Nonnull
  private final PsiClass classToSerialize;
  @Nullable
  private final PsiMethod constructor;

  public FieldAccessProvider( @Nonnull PsiClass classToSerialize ) {
    this.classToSerialize = classToSerialize;
    constructor = findLongestConstructor( classToSerialize );
  }

  @Nullable
  public PsiMethod getConstructor() {
    return constructor;
  }

  @Nonnull
  public PsiClass getClassToSerialize() {
    return classToSerialize;
  }

  @Nonnull
  public FieldSetter getFieldAccess( @Nonnull PsiField field ) {
    @Nullable FieldSetter.ConstructorFieldSetter constructorFieldAccess = getConstructorAccess( field );
    if ( constructorFieldAccess != null ) {
      return constructorFieldAccess;
    }

    return findSetter( field );
  }

  @Nullable
  private FieldSetter.ConstructorFieldSetter getConstructorAccess( @Nonnull PsiField field ) {
    if ( constructor == null ) {
      return null;
    }
    for ( PsiParameter psiParameter : constructor.getParameterList().getParameters() ) {
      PsiType type = psiParameter.getType();
      String name = psiParameter.getName();

      if ( !field.getName().equals( name ) ) {
        continue;
      }

      if ( !field.getType().equals( type ) ) {
        continue;
      }

      return new FieldSetter.ConstructorFieldSetter( constructor.getParameterList().getParameterIndex( psiParameter ) );
    }

    return null;
  }

  @Nonnull
  private static FieldSetter findSetter( @Nonnull PsiField field ) {
    @Nullable PsiMethod setter = PropertyUtil.findSetterForField( field );
    if ( setter != null ) {
      return new FieldSetter.SetterFieldSetter( setter.getName() );
    }
    return new FieldSetter.SetterFieldSetter( PropertyUtil.suggestSetterName( field ) );
  }

  @Nonnull
  private Project getProject() {
    return classToSerialize.getProject();
  }

  @javax.annotation.Nullable
  private static PsiMethod findLongestConstructor( @Nonnull PsiClass classToSerialize ) {
    PsiMethod bestConstructor = null;

    for ( PsiMethod constructor : classToSerialize.getConstructors() ) {
      if ( bestConstructor == null ) {
        bestConstructor = constructor;
        continue;
      }

      if ( constructor.getParameterList().getParameters().length > bestConstructor.getParameterList().getParameters().length ) {
        bestConstructor = constructor;
      }
    }

    return bestConstructor;
  }
}
