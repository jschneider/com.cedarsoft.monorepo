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

import com.cedarsoft.exec.Executer;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.sun.tools.xjc.api.util.APTClassLoader;
import com.sun.tools.xjc.api.util.ToolsJarNotFoundException;
import org.apache.commons.io.FileUtils;
import org.fest.reflect.core.Reflection;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

/**
 * Abstract base class for Generators.
 * Handles options and classloader issues
 */
public abstract class AbstractGenerator {
  @NonNls
  public static final String GENERATE_METHOD_NAME = "generate";


  protected void run( @NotNull File domainSourceFile, @NotNull File destination, @NotNull File testDestination, @NotNull PrintStream logOut ) throws ToolsJarNotFoundException, ClassNotFoundException, IOException, InterruptedException {
    GeneratorConfiguration configuration = new GeneratorConfiguration( domainSourceFile, destination, testDestination, new PrintWriter( logOut ) );

    File tmpDestination = createEmptyTmpDir();
    File tmpTestDestination = createEmptyTmpDir();

    GeneratorConfiguration tmpConfiguration = new GeneratorConfiguration( domainSourceFile, tmpDestination, tmpTestDestination, new PrintWriter( logOut ) );

    logOut.println( "Generating serializer for <" + domainSourceFile.getAbsolutePath() + ">" );
    logOut.println( "\tSerializer is created in <" + destination.getAbsolutePath() + ">" );
    logOut.println( "\tSerializer tests are created in <" + testDestination.getAbsolutePath() + ">" );

    //Now start the generator
    run( tmpConfiguration );
    logOut.println( "Generation finished!" );

    transferFiles( tmpConfiguration, configuration );

    logOut.println( "Cleaning up..." );
    FileUtils.deleteDirectory( tmpDestination );
    FileUtils.deleteDirectory( tmpTestDestination );
  }

  public void run( @NotNull GeneratorConfiguration configuration ) throws ToolsJarNotFoundException, ClassNotFoundException {
    run( configuration, createRunner() );
  }

  public void run( @NotNull Class<?> runnerType, @NotNull GeneratorConfiguration configuration ) throws ToolsJarNotFoundException, ClassNotFoundException {
    run( configuration, createRunner( runnerType ) );
  }

  public void run( @NotNull GeneratorConfiguration configuration, @NotNull Object runner ) {
    Reflection.method( GENERATE_METHOD_NAME ).withParameterTypes( GeneratorConfiguration.class ).in( runner ).invoke( configuration );
  }

  @NotNull
  public Object createRunner() throws ToolsJarNotFoundException, ClassNotFoundException {
    Class<?> runnerType = getRunnerType();
    return createRunner( runnerType );
  }

  public Object createRunner( @NotNull Class<?> runnerType ) {
    return Reflection.constructor().in( runnerType ).newInstance();
  }

  @NotNull
  public Class<?> getRunnerType() throws ToolsJarNotFoundException, ClassNotFoundException {
    ClassLoader aptClassLoader = createAptClassLoader();
    Thread.currentThread().setContextClassLoader( aptClassLoader );

    return aptClassLoader.loadClass( getRunnerClassName() );
  }

  /**
   * Transfers the files
   *
   * @param tmpConfiguration the temporary configuration (the source)
   * @param configuration    the configuration (the target)
   * @throws IOException
   * @throws InterruptedException
   */
  public void transferFiles( @NotNull GeneratorConfiguration tmpConfiguration, @NotNull GeneratorConfiguration configuration ) throws IOException, InterruptedException {
    transferFiles( tmpConfiguration.getDestination(), configuration.getDestination() );
    transferFiles( tmpConfiguration.getTestDestination(), configuration.getTestDestination() );
  }

  @NotNull
  public APTClassLoader createAptClassLoader() throws ToolsJarNotFoundException {
    return createAptClassLoader( getDefaultClassLoader() );
  }

  @NotNull
  public APTClassLoader createAptClassLoader( @NotNull ClassLoader defaultClassLoader ) throws ToolsJarNotFoundException {
    return new APTClassLoader( defaultClassLoader, getPackagePrefixes().toArray( new String[0] ) );
  }

  @NotNull
  public ClassLoader getDefaultClassLoader() {
    ClassLoader defaultClassLoader = getClass().getClassLoader();
    if ( defaultClassLoader == null ) {
      defaultClassLoader = ClassLoader.getSystemClassLoader();
    }
    return defaultClassLoader;
  }

  @NotNull
  @NonNls
  protected List<? extends String> getPackagePrefixes() {
    return ImmutableList.of(
      "com.cedarsoft.codegen.",
      "com.sun.istack.tools.",
      "com.sun.tools.apt.",
      "com.sun.tools.javac.",
      "com.sun.tools.javadoc.",
      "com.sun.mirror."
    );
  }

  private void transferFiles( @NotNull File sourceDir, @NotNull File destination ) throws IOException, InterruptedException {
    Collection<? extends File> serializerFiles;
    serializerFiles = FileUtils.listFiles( sourceDir, new String[]{"java"}, true );
    for ( File serializerFile : serializerFiles ) {
      String relativePath = calculateRelativePath( sourceDir, serializerFile );
      System.out.println( "--> " + relativePath );

      File targetFile = new File( destination, relativePath );
      System.out.println( "exists: " + targetFile.exists() );
      if ( targetFile.exists() ) {
        Executer executer = new Executer( new ProcessBuilder( "meld", targetFile.getAbsolutePath(), serializerFile.getAbsolutePath() ) );
        executer.execute();
      } else {
        Files.move( serializerFile, targetFile );
      }
    }
  }

  @NotNull
  @NonNls
  private static String calculateRelativePath( @NotNull File dir, @NotNull File serializerFile ) throws IOException {
    return serializerFile.getCanonicalPath().substring( dir.getCanonicalPath().length() + 1 );
  }

  @NotNull
  public static File createEmptyTmpDir() {
    return Files.createTempDir();
  }

  @NotNull
  @NonNls
  protected abstract String getRunnerClassName();

  /**
   * The runner interface
   */
  public interface Runner {
    /**
     * Generates the code
     *
     * @param configuration the configuration
     * @throws Exception
     */
    void generate( @NotNull GeneratorConfiguration configuration ) throws Exception;
  }
}
