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
package com.cedarsoft.osgi.validator;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Validator {
  @Nonnull
  private final String projectId;

  @Nonnull
  private final Set<String> partsToIgnore;

  public Validator( @Nonnull String projectId, @Nonnull Set<? extends String> partsToIgnore ) {
    this.projectId = projectId;
    this.partsToIgnore = ImmutableSet.copyOf( partsToIgnore );
  }

  @Nonnull
  public Set<String> getPartsToIgnore() {
    //noinspection ReturnOfCollectionOrArrayField
    return partsToIgnore;
  }

  @Nonnull
  public String getProjectId() {
    return projectId;
  }

  public void isValid( @Nonnull String relativePath ) throws ValidationFailedException{
    List<? extends String> projectIdParts = splitProjectId( projectId );
    int projectIdIndex = 0;

    //Every part within the package should be reflected by the project id parts
    List<? extends String> splitPath = splitPath( relativePath );
    for ( int pathIndex = 0; pathIndex < splitPath.size(); ) {
      String pathElement = splitPath.get( pathIndex );

      String projectIdPart = projectIdParts.get( projectIdIndex );

      //Direct hit, continue!
      if ( pathElement.equals( projectIdPart ) ) {
        projectIdIndex++;

        if ( projectIdIndex == projectIdParts.size() ) {
          return;
        }

        pathIndex++;
        continue;
      }


      //Maybe the project id part is ignored(?)
      if ( partsToIgnore.contains( projectIdPart ) ) {
        projectIdIndex++;

        //Do *not* increase the index of the path
        if ( projectIdIndex == projectIdParts.size() ) {
          return;
        }
        continue;
      }

      //Maybe the part is a duplicate(?)
      if ( projectIdIndex > 0 && projectIdPart.equals( projectIdParts.get( projectIdIndex - 1 ) ) ) {
        projectIdIndex++;

        //Do *not* increase the index of the path
        if ( projectIdIndex == projectIdParts.size() ) {
          return;
        }
        continue;
      }

      throw new ValidationFailedException( relativePath, splitPath, pathIndex, "Expected <" + projectIdPart + ">." );
    }

    //check whether the remaining parts might be ignored
    while ( projectIdIndex < projectIdParts.size() ) {

      if ( !partsToIgnore.contains( projectIdParts.get( projectIdIndex ) ) ) {
        throw new ValidationFailedException( relativePath, splitPath, -1, "Too short for project id <" + projectId + ">" );
      }
      projectIdIndex++;
    }
  }


  @Nonnull
  private static List<? extends String> splitPath( @Nonnull String fileName ) {
    Splitter splitter = Splitter.on( File.separator ).omitEmptyStrings();
    return Lists.newArrayList( splitter.split( fileName ) );
  }

  @Nonnull
  private static List<? extends String> splitProjectId( @Nonnull String projectId ) {
    Splitter splitter = Splitter.on( new PackageSeparatorCharMatcher() ).omitEmptyStrings();
    return Lists.newArrayList( splitter.split( projectId ) );
  }

  private static class PackageSeparatorCharMatcher extends CharMatcher {
    @Override
    public boolean matches( char c ) {
      return c == '.' || c == '-';
    }
  }
}
