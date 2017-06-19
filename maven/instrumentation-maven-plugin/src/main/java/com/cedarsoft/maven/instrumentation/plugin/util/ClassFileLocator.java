/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cedarsoft.maven.instrumentation.plugin.util;

import javassist.ClassPool;
import javassist.LoaderClassPath;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.maven.plugin.logging.Log;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public final class ClassFileLocator {
  @Nonnull
  private final Log log;
  @Nonnull
  private final ClassLoader classLoader;

  public ClassFileLocator( @Nonnull Log log, @Nonnull ClassLoader classLoader ) {
    this.log = log;
    this.classLoader = classLoader;
  }

  @Nonnull
  public Collection<? extends ClassFile> findClasses( @Nonnull final File targetDir ) {
    if ( !targetDir.isDirectory() ) {
      throw new IllegalArgumentException(
        "targetDir must be a directory: " + targetDir );
    }

    return recursivelyFindClasses( targetDir );
  }

  @Nonnull
  private Collection<? extends ClassFile> recursivelyFindClasses( @Nonnull final File targetDir ) {
    final FileFilter directoryFilter = FileFilterUtils.directoryFileFilter();
    final File[] subDirs = targetDir.listFiles( directoryFilter );
    final Collection<ClassFile> classesFound = new ArrayList<ClassFile>();
    for ( final File subDir : subDirs ) {
      classesFound.addAll( recursivelyFindClasses( subDir ) );
    }

    classesFound.addAll( findClassFilesInDirectory( targetDir ) );

    return classesFound;
  }

  @Nonnull
  private Collection<? extends ClassFile> findClassFilesInDirectory( @Nonnull final File directory ) {
    final FileFilter classNameFilter = new ClassFileFilter();
    final File[] classFiles = directory.listFiles( classNameFilter );

    final Collection<ClassFile> classesFound = new ArrayList<ClassFile>();

    ClassPool classPool = new ClassPool( true );
    classPool.appendClassPath(new LoaderClassPath(classLoader));

    for ( final File file : classFiles ) {
      try {
        ClassFile classFile = new ClassFile( file, classLoader, classPool);
        classesFound.add( classFile );
      } catch ( final IOException e ) {
        getLog().warn( "Failed to read in file: " + file + ", it will be ignored.", e );
      }
    }
    return classesFound;
  }

  @Nonnull
  public Log getLog() {
    return log;
  }
}
