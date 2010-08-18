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

package com.cedarsoft.codegen.model;

import com.cedarsoft.codegen.TypeUtils;
import com.google.common.collect.Lists;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.TypeMirror;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class DomainObjectDescriptor {
  @NotNull
  private final List<FieldWithInitializationInfo> fieldInfos = Lists.newArrayList();
  @NotNull
  private final ClassDeclaration classDeclaration;

  public DomainObjectDescriptor( @NotNull @NonNls ClassDeclaration classDeclaration ) {
    this.classDeclaration = classDeclaration;
  }

  @NotNull
  @NonNls
  public String getQualifiedName() {
    return classDeclaration.getQualifiedName();
  }

  @NotNull
  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  public void addField( @NotNull FieldWithInitializationInfo fieldToSerialize ) {
    this.fieldInfos.add( fieldToSerialize );
  }

  @NotNull
  public List<? extends FieldWithInitializationInfo> getFieldInfos() {
    return Collections.unmodifiableList( fieldInfos );
  }

  /**
   * Returns only the field infos that are initialized using the constructor
   *
   * @return the field infos initialized within the constructor
   */
  @NotNull
  public List<? extends FieldInitializedInConstructorInfo> getFieldsInitializedInConstructor() {
    List<FieldInitializedInConstructorInfo> found = new ArrayList<FieldInitializedInConstructorInfo>();
    for ( FieldWithInitializationInfo info : fieldInfos ) {
      if ( info instanceof FieldInitializedInConstructorInfo ) {
        found.add( ( FieldInitializedInConstructorInfo ) info );
      }
    }

    Collections.sort( found, new FieldWithInitializationInfoComparator() );

    return found;
  }

  @NotNull
  public List<? extends FieldInitializedInSetterInfo> getFieldsInitializedInSetter() {
    List<FieldInitializedInSetterInfo> found = new ArrayList<FieldInitializedInSetterInfo>();
    for ( FieldWithInitializationInfo info : fieldInfos ) {
      if ( info instanceof FieldInitializedInSetterInfo ) {
        found.add( ( FieldInitializedInSetterInfo ) info );
      }
    }

    return found;
  }

  @NotNull
  public List<? extends FieldNotInitializationInfo> getFieldsNotInitialized() {
    List<FieldNotInitializationInfo> found = new ArrayList<FieldNotInitializationInfo>();
    for ( FieldWithInitializationInfo info : fieldInfos ) {
      if ( info instanceof FieldNotInitializationInfo ) {
        found.add( ( FieldNotInitializationInfo ) info );
      }
    }

    return found;
  }

  @NotNull
  public ConstructorDeclaration findBestConstructor() {
    return TypeUtils.findBestConstructor( classDeclaration );
  }

  @NotNull
  public MethodDeclaration findSetter( @NotNull @NonNls String fieldName, @NotNull TypeMirror type ) {
    return TypeUtils.findSetter( classDeclaration, fieldName, type );
  }

  @NotNull
  public MethodDeclaration findSetter( @NotNull FieldDeclaration fieldDeclaration ) {
    return TypeUtils.findSetter( classDeclaration, fieldDeclaration );
  }

  @NotNull
  public MethodDeclaration findGetterForField( @NotNull FieldDeclaration fieldDeclaration ) {
    return TypeUtils.findGetterForField( classDeclaration, fieldDeclaration );
  }

  @NotNull
  public MethodDeclaration findGetterForField( @NotNull @NonNls String simpleName, @NotNull TypeMirror type ) {
    return TypeUtils.findGetterForField( classDeclaration, simpleName, type );
  }

  @NotNull
  public FieldDeclaration findFieldDeclaration( @NotNull @NonNls String fieldName ) {
    return TypeUtils.findFieldDeclaration( classDeclaration, fieldName );
  }

  private static class FieldWithInitializationInfoComparator implements Comparator<FieldInitializedInConstructorInfo>, Serializable {
    @Override
    public int compare( FieldInitializedInConstructorInfo o1, FieldInitializedInConstructorInfo o2 ) {
      return Integer.valueOf( o1.getConstructorCallInfo().getIndex() ).compareTo( o2.getConstructorCallInfo().getIndex() );
    }
  }
}
