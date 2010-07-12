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

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.PrintWriter;

/**
 *
 */
public class GeneratorConfiguration {
  @NotNull
  private final File domainSourceFile;
  @NotNull
  private final File destination;
  @NotNull
  private final File testDestination;
  @NotNull
  private final CreationMode creationMode;
  @NotNull
  private final PrintWriter logOut;

  public GeneratorConfiguration( @NotNull File domainSourceFile, @NotNull File destination, @NotNull File testDestination, @NotNull PrintWriter logOut ) {
    this( domainSourceFile, destination, testDestination, logOut, CreationMode.ALL );
  }

  public GeneratorConfiguration( @NotNull File domainSourceFile, @NotNull File destination, @NotNull File testDestination, @NotNull PrintWriter logOut, @NotNull CreationMode creationMode ) {
    this.domainSourceFile = domainSourceFile;
    this.destination = destination;
    this.testDestination = testDestination;
    this.creationMode = creationMode;
    this.logOut = logOut;
  }

  @NotNull
  public File getDomainSourceFile() {
    return domainSourceFile;
  }

  @NotNull
  public CreationMode getCreationMode() {
    return creationMode;
  }

  @NotNull
  public File getDestination() {
    return destination;
  }

  @NotNull
  public File getTestDestination() {
    return testDestination;
  }

  @NotNull
  public PrintWriter getLogOut() {
    return logOut;
  }

  public enum CreationMode {
    ALL,
    TESTS_ONLY
  }
}
