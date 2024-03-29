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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.TypeConversionUtil;

import javax.annotation.Nonnull;

/**
 * Describes a field that has been serialized
 *
 */
public class FieldToSerialize {
  @Nonnull
  private final PsiType fieldType;
  @Nonnull
  private final PsiField field;
  @Nonnull
  private final String fieldName;
  @Nonnull
  private final FieldSetter fieldSetter;
  @Nonnull
  private final String accessor;
  @Nonnull
  private final String propertyConstant;
  @Nonnull
  private final String defaultValue;

  /**
   * If set to true a list is serialized/deserialized
   */
  private final boolean isCollection;

  @Deprecated
  public FieldToSerialize(@Nonnull PsiType fieldType, @Nonnull PsiField field, @Nonnull FieldSetter fieldSetter ) {
    this(fieldType, field, fieldSetter, false);
  }

  public FieldToSerialize(@Nonnull PsiType fieldType, @Nonnull PsiField field, @Nonnull FieldSetter fieldSetter, boolean isCollection) {
    this.fieldType = fieldType;
    this.field = field;

    assert field.getName() != null;
    this.fieldName = field.getName();

    this.fieldSetter = fieldSetter;

    this.accessor = findGetterName(field );
    this.isCollection = isCollection;
    this.propertyConstant = "PROPERTY_" + JavaCodeStyleManager.getInstance(getProject() ).suggestVariableName(VariableKind.STATIC_FINAL_FIELD, field.getName(), null, fieldType ).names[0];

    defaultValue = getDefaultValue(fieldType );
  }

  public boolean isCollection() {
    return isCollection;
  }

  @Nonnull
  public PsiField getField() {
    return field;
  }

  @Nonnull
  public FieldSetter getFieldSetter() {
    return fieldSetter;
  }

  @Nonnull
  public PsiType getFieldType() {
    return fieldType;
  }

  @Nonnull
  public String getFieldName() {
    return fieldName;
  }

  @Nonnull
  public String getAccessor() {
    return accessor;
  }

  @Nonnull
  public String getPropertyConstantName() {
    return propertyConstant;
  }

  @Nonnull
  public String getDefaultValue() {
    return defaultValue;
  }

  public boolean shallVerifyDeserialized() {
    return !PsiType.BOOLEAN.equals( fieldType );
  }

  @Nonnull
  private Project getProject() {
    return field.getProject();
  }

  @Nonnull
  public String getFieldTypeBoxed() {
    return DelegatingSerializer.box( getFieldType() );
  }

  public boolean isPrimitive() {
    return isPrimitive( getFieldType() );
  }

  /**
   * Returns the default value for a given type
   *
   * @param fieldType the field type
   * @return the default value
   */
  @Nonnull
  private static String getDefaultValue( @Nonnull PsiType fieldType ) {
    if ( isPrimitive( fieldType ) ) {
      if ( TypeConversionUtil.isBooleanType( fieldType ) ) {
        return "false";
      }
      if ( PsiType.CHAR.equals( fieldType ) ) {
        return "(char)-1";
      }

      return "-1";
    } else {
      return "null";
    }
  }

  /**
   * Whether the given field type is a primitive
   *
   * @param fieldType the field type
   * @return true if the given type is a primitive, false otherwise
   */
  public static boolean isPrimitive( @Nonnull PsiType fieldType ) {
    return TypeConversionUtil.isPrimitiveAndNotNull( fieldType );
  }

  /**
   * Returns the getter for a given field
   *
   * @param field the field
   * @return the getter name
   */
  @Nonnull
  public static String findGetterName( @Nonnull PsiField field ) {
    PsiMethod getter = PropertyUtil.findGetterForField( field );
    if ( getter != null ) {
      return getter.getName() + "()";

    }
    return "get" + StringUtil.capitalizeWithJavaBeanConvention( field.getName() ) + "()";
  }
}
