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
package com.cedarsoft.serialization.generator.intellij.model;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiClass;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SerializerModel {
  @Nonnull
  private final ImmutableList<? extends FieldToSerialize> fieldToSerializeEntries;

  @Nonnull
  private final ImmutableList<? extends DelegatingSerializer> delegatingSerializerEntries;

  @Nonnull
  private final PsiClass classToSerialize;

  public SerializerModel( @Nonnull PsiClass classToSerialize, @Nonnull Collection<? extends FieldToSerialize> fieldToSerializeEntries, @Nonnull Collection<? extends DelegatingSerializer> delegatingSerializerEntries ) {
    this.classToSerialize = classToSerialize;
    this.fieldToSerializeEntries = ImmutableList.copyOf( fieldToSerializeEntries );
    this.delegatingSerializerEntries = ImmutableList.copyOf( delegatingSerializerEntries );
  }

  @Nonnull
  public PsiClass getClassToSerialize() {
    return classToSerialize;
  }

  @Nonnull
  public String generateSerializerClassName() {
    return getClassToSerialize().getName() + "Serializer";
  }

  @Nonnull
  public String generateSerializerTestClassName() {
    return getClassToSerialize().getName() + "SerializerTest";
  }

  @Nonnull
  public String generateSerializerVersionTestClassName() {
    return getClassToSerialize().getName() + "SerializerVersionTest";
  }

  @Nonnull
  public ImmutableList<? extends FieldToSerialize> getFieldToSerializeEntries() {
    //noinspection ReturnOfCollectionOrArrayField
    return fieldToSerializeEntries;
  }

  @Nonnull
  public ImmutableList<? extends DelegatingSerializer> getDelegatingSerializerEntries() {
    //noinspection ReturnOfCollectionOrArrayField
    return delegatingSerializerEntries;
  }

  @Nonnull
  public String getClassToSerializeQualifiedName() {
    String qualifiedName = getClassToSerialize().getQualifiedName();
    if ( qualifiedName == null ) {
      throw new IllegalStateException( "No qualified name found for <" + getClassToSerialize() + ">" );
    }

    return qualifiedName;
  }
}
