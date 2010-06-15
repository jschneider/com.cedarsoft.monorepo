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

package com.cedarsoft.exec;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class that is able to start a new java process
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class JavaCommandBuilder {
  @NonNls
  @NotNull
  private String javaBin = "java";
  @NotNull
  private final List<String> classPathElements = new ArrayList<String>();
  @NotNull
  @NonNls
  private final String mainClass;
  @NotNull
  private final List<String> vmProperties = new ArrayList<String>();
  @NotNull
  private final List<String> arguments = new ArrayList<String>();

  /**
   * <p>Constructor for JavaCommandBuilder.</p>
   *
   * @param mainClass a {@link java.lang.String} object.
   */
  public JavaCommandBuilder( @NotNull @NonNls String mainClass ) {
    this.mainClass = mainClass;
  }

  /**
   * <p>Getter for the field <code>classPathElements</code>.</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<String> getClassPathElements() {
    return Collections.unmodifiableList( classPathElements );
  }

  /**
   * <p>Setter for the field <code>classPathElements</code>.</p>
   *
   * @param classPathElements a {@link java.lang.String} object.
   */
  public void setClassPathElements( @NotNull @NonNls String... classPathElements ) {
    this.classPathElements.clear();
    for ( String element : classPathElements ) {
      if ( element == null || element.length() == 0 ) {
        continue;
      }
      this.classPathElements.add( element );
    }
  }

  /**
   * <p>Getter for the field <code>javaBin</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  @NonNls
  public String getJavaBin() {
    return javaBin;
  }

  /**
   * Returns the classpath
   *
   * @return the classpath
   */
  @Nullable
  @NonNls
  public String getClassPath() {
    if ( classPathElements.isEmpty() ) {
      return null;
    }
    StringBuilder stringBuilder = new StringBuilder();
    for ( Iterator<String> it = classPathElements.iterator(); it.hasNext(); ) {
      String classPathElement = it.next();
      if ( classPathElement == null || classPathElement.length() == 0 ) {
        continue;
      }
      stringBuilder.append( classPathElement );
      if ( it.hasNext() ) {
        stringBuilder.append( File.pathSeparator );
      }
    }

    String classPath = stringBuilder.toString();
    if ( classPath.contains( " " ) ) {
      if ( !System.getProperty( "os.name" ).toLowerCase().contains( "windows" ) ) {
        System.err.println( "WARNING: Be carefull! Added \" to the classpath. May be incompatible!" );
      }
      return '\"' + classPath + '\"';
    } else {
      return classPath;
    }
  }

  /**
   * <p>Getter for the field <code>mainClass</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  @NonNls
  public String getMainClass() {
    return mainClass;
  }

  /**
   * <p>addClassPathElement</p>
   *
   * @param classPathElement a {@link java.lang.String} object.
   */
  public void addClassPathElement( @NotNull @NonNls String classPathElement ) {
    classPathElements.add( classPathElement );
  }

  /**
   * <p>Getter for the field <code>vmProperties</code>.</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<String> getVmProperties() {
    return Collections.unmodifiableList( vmProperties );
  }

  /**
   * <p>Setter for the field <code>vmProperties</code>.</p>
   *
   * @param vmProperties a {@link java.lang.String} object.
   */
  public void setVmProperties( @NonNls @NotNull String... vmProperties ) {
    this.vmProperties.clear();
    for ( String vmProperty : vmProperties ) {
      if ( vmProperty == null || vmProperty.length() == 0 ) {
        continue;
      }
      this.vmProperties.add( vmProperty );
    }
  }

  /**
   * <p>addVmProperty</p>
   *
   * @param vmProeprty a {@link java.lang.String} object.
   */
  public void addVmProperty( @NotNull @NonNls String vmProeprty ) {
    vmProperties.add( vmProeprty );
  }

  /**
   * <p>addArgument</p>
   *
   * @param argument a {@link java.lang.String} object.
   */
  public void addArgument( @NotNull @NonNls String argument ) {
    this.arguments.add( argument );
  }

  /**
   * <p>Setter for the field <code>arguments</code>.</p>
   *
   * @param arguments a {@link java.lang.String} object.
   */
  public void setArguments( @NotNull @NonNls String... arguments ) {
    this.arguments.clear();
    for ( String argument : arguments ) {
      if ( argument == null || argument.length() == 0 ) {
        continue;
      }
      this.arguments.add( argument );
    }
  }

  /**
   * <p>Getter for the field <code>arguments</code>.</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  @NonNls
  public List<String> getArguments() {
    return Collections.unmodifiableList( arguments );
  }

  /**
   * <p>getCommandLineElements</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  @NonNls
  public List<String> getCommandLineElements() {
    List<String> elements = new ArrayList<String>();
    elements.add( getJavaBin() );

    for ( String property : getVmProperties() ) {
      elements.add( "-D" + property );
    }

    String classPath = getClassPath();
    if ( classPath != null ) {
      elements.add( "-cp" );
      elements.add( classPath );
    }

    elements.add( getMainClass() );

    for ( String argument : getArguments() ) {
      elements.add( argument );
    }

    return elements;
  }

  /**
   * Returns the command line as string. This method should only be used
   * for debugging purposes.
   * For creating a process use {@link #getCommandLineElements()} instead.
   *
   * @return the command line as string
   *
   * @see #getCommandLineElements()
   */
  @NotNull
  @NonNls
  public String getCommandLine() {
    StringBuilder stringBuilder = new StringBuilder();
    for ( String element : getCommandLineElements() ) {
      stringBuilder.append( element );
      stringBuilder.append( ' ' );
    }

    return stringBuilder.toString().trim();
  }
}
