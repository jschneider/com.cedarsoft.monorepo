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

package com.cedarsoft.codegen;

import com.cedarsoft.NotFoundException;
import com.google.common.base.Splitter;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JType;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.WildcardType;
import com.sun.mirror.util.Types;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Offers utility methods related to types
 */
public class TypeUtils {
  private TypeUtils() {
  }

  @NotNull
  private static final ThreadLocal<Types> TYPES = new ThreadLocal<Types>();

  @NotNull
  public static Types getTypes() {
    Types resolved = TYPES.get();
    if ( resolved == null ) {
      throw new IllegalStateException( "No types object found!" );
    }
    return resolved;
  }

  public static void setTypes( @NotNull Types types ) {
    TYPES.set( types );
  }

  public static boolean isStatic( @NotNull Declaration fieldDeclaration ) {
    for ( Modifier modifier : fieldDeclaration.getModifiers() ) {
      if ( modifier == Modifier.STATIC ) {
        return true;
      }
    }
    return false;
  }

  @NotNull
  public static TypeMirror getErasure( @NotNull TypeMirror type ) {
    return getTypes().getErasure( type );
  }

  @NotNull
  public static JClass getCollectionParam( @NotNull JClass type ) {
    if ( !isCollectionType( type ) ) {
      throw new IllegalArgumentException( type + " is not a collection type" );
    }

    List<JClass> params = type.getTypeParameters();
    if ( params.size() != 1 ) {
      throw new IllegalArgumentException( "Invalid type parameters cound for " + type );
    }

    return params.get( 0 );
  }

  @NotNull
  public static TypeMirror getCollectionParam( @NotNull TypeMirror type ) throws NotFoundException {
    if ( !isCollectionType( type ) ) {
      throw new IllegalArgumentException( "Invalid type: " + type );
    }

    return getFirstTypeParam( ( DeclaredType ) type );
  }

  @NotNull
  private static TypeMirror getFirstTypeParam( @NotNull DeclaredType type ) throws NotFoundException {
    Collection<TypeMirror> typeArguments = type.getActualTypeArguments();
    if ( typeArguments.isEmpty() ) {
      throw new NotFoundException( "No typeArguments found for <" + type + ">" );
    }

    return typeArguments.iterator().next();
  }

  public static boolean isCollectionType( @NotNull TypeMirror type ) {
    if ( !( type instanceof DeclaredType ) ) {
      return false;
    }

    TypeDeclaration declaredType = ( ( DeclaredType ) type ).getDeclaration();
    if ( declaredType == null ) {
      throw new IllegalArgumentException( "No declaration found for <" + type + ">" );
    }

    if ( isCollection( declaredType.getQualifiedName() ) ) {
      return true;
    }

    for ( InterfaceType interfaceType : declaredType.getSuperinterfaces() ) {
      if ( isCollection( interfaceType.getDeclaration().getQualifiedName() ) ) {
        return true;
      }
    }

    return false;
  }

  public static boolean isCollectionType( @NotNull JType type ) {
    return implementsInterface( type, Collection.class );
  }

  public static boolean isSetType( @NotNull JType type ) {
    return implementsInterface( type, Set.class );
  }

  private static boolean implementsInterface( @NotNull JType type, @NotNull Class<?> daClass ) {
    JType erasure = type.erasure();
    if ( erasure.fullName().equals( daClass.getName() ) ) {
      return true;
    }

    Iterator<JClass> implementedIterator = ( ( JClass ) erasure )._implements();
    while ( implementedIterator.hasNext() ) {
      JClass implemented = implementedIterator.next();

      if ( implemented.fullName().equals( daClass.getName() ) ) {
        return true;
      }
    }

    return false;
  }

  public static boolean isSetType( @NotNull TypeMirror type ) {
    if ( !( type instanceof DeclaredType ) ) {
      return false;
    }

    TypeDeclaration declaredType = ( ( DeclaredType ) type ).getDeclaration();
    if ( declaredType == null ) {
      throw new IllegalArgumentException( "No declaration found for <" + type + ">" );
    }

    if ( isSet( declaredType.getQualifiedName() ) ) {
      return true;
    }

    for ( InterfaceType interfaceType : declaredType.getSuperinterfaces() ) {
      if ( isSet( interfaceType.getDeclaration().getQualifiedName() ) ) {
        return true;
      }
    }

    return false;
  }

  public static boolean isAssignable( TypeMirror t1, TypeMirror t2 ) {
    return getTypes().isAssignable( t1, t2 );
  }

  public static boolean mightBeConstructorCallFor( @NotNull TypeMirror parameterType, @NotNull TypeMirror fieldType ) {
    return isAssignable( parameterType, fieldType ) || isAssignable( fieldType, parameterType );
  }

  @NotNull
  public static MethodDeclaration findSetter( @NotNull ClassDeclaration classDeclaration, @NotNull FieldDeclaration fieldDeclaration ) {
    return findSetter( classDeclaration, fieldDeclaration.getSimpleName(), fieldDeclaration.getType() );
  }

  @NotNull
  public static MethodDeclaration findSetter( @NotNull ClassDeclaration classDeclaration, @NotNull @NonNls String fieldName, @NotNull TypeMirror type ) throws IllegalArgumentException {
    String expectedName = NamingSupport.createSetter( fieldName );

    for ( MethodDeclaration methodDeclaration : classDeclaration.getMethods() ) {
      if ( !methodDeclaration.getSimpleName().equals( expectedName ) ) {
        continue;
      }

      if ( methodDeclaration.getParameters().size() != 1 ) {
        throw new IllegalArgumentException( "Expected one parameter. But was <" + methodDeclaration.getParameters() + ">" );
      }

      ParameterDeclaration parameterDeclaration = methodDeclaration.getParameters().iterator().next();
      if ( !isAssignable( type, parameterDeclaration.getType() ) ) {
        throw new IllegalArgumentException( "Invalid parameter type for <" + expectedName + ">. Was <" + parameterDeclaration.getType() + "> but expected <" + type + ">" );
      }

      return methodDeclaration;
    }

    throw new IllegalArgumentException( "No method declaration found for <" + expectedName + ">" );
  }

  public static MethodDeclaration findGetterForField( @NotNull ClassDeclaration classDeclaration, @NotNull FieldDeclaration fieldDeclaration ) {
    return findGetterForField( classDeclaration, fieldDeclaration.getSimpleName(), fieldDeclaration.getType() );
  }

  /**
   * @param classDeclaration the class declaration
   * @param simpleName       the simple name
   * @param type             the type
   * @return the getter declaration
   *
   * @noinspection TypeMayBeWeakened
   */
  public static MethodDeclaration findGetterForField( @NotNull ClassDeclaration classDeclaration, @NotNull @NonNls String simpleName, @NotNull TypeMirror type ) {
    String expectedName = "get" + simpleName.substring( 0, 1 ).toUpperCase() + simpleName.substring( 1 );

    for ( MethodDeclaration methodDeclaration : classDeclaration.getMethods() ) {
      if ( methodDeclaration.getSimpleName().equals( expectedName ) ) {
        TypeMirror returnType = methodDeclaration.getReturnType();
        if ( isAssignable( type, returnType ) ) {
          return methodDeclaration;
        } else {
          throw new IllegalArgumentException( "Invalid return types for <" + expectedName + ">. Was <" + returnType + "> but expected <" + type + ">" );
        }
      }
    }

    throw new IllegalArgumentException( "No method declaration found for <" + expectedName + ">" );
  }


  /**
   * @param classDeclaration the class declaration
   * @param fieldName        the field name
   * @return the field declaration
   *
   * @noinspection TypeMayBeWeakened
   */
  @NotNull
  public static FieldDeclaration findFieldDeclaration( @NotNull ClassDeclaration classDeclaration, @NotNull @NonNls String fieldName ) {
    for ( FieldDeclaration fieldDeclaration : classDeclaration.getFields() ) {
      if ( fieldDeclaration.getSimpleName().equals( fieldName ) ) {
        return fieldDeclaration;
      }
    }

    throw new IllegalArgumentException( "No field declaration found for <" + fieldName + ">" );
  }

  public static boolean isType( @NotNull TypeMirror typeMirror, @NotNull Class<?> expected ) {
    @NonNls
    String typeAsName = expected.getName();
    return typeMirror.toString().equals( typeAsName );
  }

  public static boolean isType( @NotNull JType type, @NotNull Class<?> expected ) {
    return removeWildcard( type ).equals( expected.getName() );
  }

  private static boolean isCollection( @NotNull @NonNls String qualifiedName ) {
    return qualifiedName.equals( Collection.class.getName() );
  }

  private static boolean isSet( @NotNull @NonNls String qualifiedName ) {
    return qualifiedName.equals( Set.class.getName() );
  }

  /**
   * Returns true if the given type is a simple type (String, Integer...)
   *
   * @param type the type
   * @return true if the given type is a simple type, false otherwise
   */
  public static boolean isSimpleType( @NotNull TypeMirror type ) {
    return SIMPLE_TYPE_NAMES.contains( type.toString() );
  }

  public static boolean isSimpleType( @NotNull JType type ) {
    return SIMPLE_TYPE_NAMES.contains( type.fullName() );
  }

  @NotNull
  private static final Set<? extends Class<?>> SIMPLE_TYPES;

  static {
    Set<Class<?>> types = new HashSet<Class<?>>();
    types.add( String.class );
    types.add( Integer.class );
    types.add( Integer.TYPE );
    types.add( Long.class );
    types.add( Long.TYPE );
    types.add( Float.class );
    types.add( Float.TYPE );
    types.add( Double.class );
    types.add( Double.TYPE );
    types.add( Short.class );
    types.add( Short.TYPE );
    types.add( Byte.class );
    types.add( Byte.TYPE );
    types.add( Character.class );
    types.add( Character.TYPE );
    types.add( Boolean.class );
    types.add( Boolean.TYPE );

    SIMPLE_TYPES = Collections.unmodifiableSet( types );
  }

  @NotNull
  private static final Set<? extends String> SIMPLE_TYPE_NAMES;

  static {
    Set<String> names = new HashSet<String>();
    for ( Class<?> supportedType : SIMPLE_TYPES ) {
      names.add( supportedType.getName() );
    }
    SIMPLE_TYPE_NAMES = Collections.unmodifiableSet( names );
  }

  public static boolean isWildcardType( @NotNull TypeMirror type ) {
    return type instanceof WildcardType;
  }

  @NotNull
  @NonNls
  public static String removeWildcard( @NotNull JType classWithWildcard ) {
    if ( classWithWildcard != classWithWildcard.erasure() ) {
      throw new IllegalArgumentException( "Invalid type - cannot remove wildcard. Call erasure() first: " + classWithWildcard.fullName() );
    }

    if ( classWithWildcard.fullName().contains( "?" ) ) {
      String fullName = classWithWildcard.fullName();

      Iterable<String> parts = Splitter.on( " " ).split( fullName );

      String last = null;
      for ( String part : parts ) {
        last = part;
      }
      assert last != null;
      return last;
    }

    return classWithWildcard.fullName();
  }
}
