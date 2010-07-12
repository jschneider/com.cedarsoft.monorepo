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

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 */
public class GeneratorConfiguration {
  @NotNull
  private final List<? extends File> domainSourceFiles;
  @NotNull
  private final File destination;
  @NotNull
  private final File testDestination;
  @NotNull
  private final CreationMode creationMode;
  @NotNull
  private final PrintWriter logOut;

  public GeneratorConfiguration( @NotNull Iterable<? extends File> domainSourceFiles, @NotNull File destination, @NotNull File testDestination, @NotNull PrintWriter logOut ) {
    this( domainSourceFiles, destination, testDestination, logOut, CreationMode.ALL );
  }

  public GeneratorConfiguration( @NotNull Iterable<? extends File> domainSourceFiles, @NotNull File destination, @NotNull File testDestination, @NotNull PrintWriter logOut, @NotNull CreationMode creationMode ) {
    this.domainSourceFiles = ImmutableList.copyOf( domainSourceFiles );
    this.destination = destination;
    this.testDestination = testDestination;
    this.creationMode = creationMode;
    this.logOut = logOut;
  }

  @NotNull
  public List<? extends File> getDomainSourceFiles() {
    //noinspection ReturnOfCollectionOrArrayField
    return domainSourceFiles;
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
    ALL( true, true ),
    NO_TESTS( true, false ),
    TESTS_ONLY( false, true );

    private final boolean createTests;
    private final boolean create;

    CreationMode( boolean create, boolean createTests ) {
      this.create = create;
      this.createTests = createTests;
    }

    public boolean isCreateTests() {
      return createTests;
    }

    public boolean isCreate() {
      return create;
    }

    public static CreationMode get( boolean create, boolean tests ) {
      if ( create && tests ) {
        return ALL;
      }

      if ( create && !tests ) {
        return NO_TESTS;
      }

      if ( tests ) {
        return TESTS_ONLY;
      }

      throw new IllegalArgumentException();
    }
  }
}