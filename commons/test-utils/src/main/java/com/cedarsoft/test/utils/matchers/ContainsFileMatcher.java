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

package com.cedarsoft.test.utils.matchers;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
public class ContainsFileMatcher implements Predicate<File> {
  @Nonnull
  private final String relativeFilePath;

  public ContainsFileMatcher( @Nonnull String relativeFilePath ) {
    this.relativeFilePath = relativeFilePath;
  }

  @Override
  public boolean test(File file) {
    if ( !file.isDirectory() ) {
      return false;
    }

    File searched = new File( file, relativeFilePath );
    return searched.isFile();
  }

  @Nonnull
  public static ContainsFileMatcher containsFile( @Nonnull String relativeFilePath ) {
    return new ContainsFileMatcher( relativeFilePath );
  }

  @Nonnull
  public static Predicate<File> empty() {
    return new Predicate<File>() {
      @Override
      public boolean test(File file) {
        @Nullable String[] list = file.list();
        if (list == null) {
          throw new IllegalStateException("Can not list " + file.getAbsolutePath());
        }
        return list.length == 0;
      }
    };
  }

  @Nonnull
  public static Predicate<File> containsFiles(@Nonnull String... relativeFilePaths) {
    List<Predicate<? super File>> matchers = new ArrayList<>();
    for ( String relativeFilePath : relativeFilePaths ) {
      matchers.add( containsFile( relativeFilePath ) );
    }

    return new AndMatcher<File>(matchers);
  }

  @Nonnull
  public static String toTree( @Nonnull final File dir ) {
    List<File> files = Lists.newArrayList(FileUtils.iterateFiles(dir, TrueFileFilter.TRUE, TrueFileFilter.TRUE ));

    List<String> names = Lists.newArrayList( Lists.transform( files, new Function<File, String>() {
      @Override
      public String apply(File input) {
        return input.getPath().substring(dir.getPath().length() + 1);
      }
    } ) );
    Collections.sort( names );

    Joiner joiner = Joiner.on( "\n" );
    return joiner.join( names );
  }


  @Nonnull
  public static String toMessage( @Nonnull final File dir ) {
    return dir.getAbsolutePath() + ":\n" + toTree( dir );
  }

}
