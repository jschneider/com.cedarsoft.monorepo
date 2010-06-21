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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.fest.reflect.core.Reflection;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Abstract base class for Generators.
 * Handles options and classloader issues
 */
public abstract class AbstractGenerator {
  @NonNls
  public static final String HELP_OPTION = "h";
  @NonNls
  public static final String OPTION_DESTINATION = "d";
  @NonNls
  public static final String OPTION_TEST_DESTINATION = "t";


  protected void printError( Options options, String errorMessage ) {
    System.out.println( errorMessage );
    printHelp( options );
  }

  protected void printHelp( @NotNull Options options ) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "gen-ser -d <serializer dest dir> -t <test dest dir> path-to-class", options );
  }

  @NotNull
  protected Options buildOptions() {
    Options options = new Options();
    {
      Option option = new Option( OPTION_DESTINATION, "destination", true, "the output directory for the created classes" );
      option.setRequired( true );
      options.addOption( option );
    }

    {
      Option option = new Option( OPTION_TEST_DESTINATION, "test-destination", true, "the output directory for the created tests" );
      option.setRequired( true );
      options.addOption( option );
    }
    options.addOption( HELP_OPTION, "help", false, "display this use message" );

    return options;
  }


  public void run( @NotNull @NonNls String[] args ) throws Exception {
    Options options = buildOptions();
    CommandLine commandLine;
    try {
      commandLine = new GnuParser().parse( options, args );
    } catch ( MissingOptionException e ) {
      printError( options, e.getMessage() );
      return;
    }

    if ( commandLine.hasOption( HELP_OPTION ) ) {
      printHelp( options );
      return;
    }

    List<? extends String> domainObjectNames = commandLine.getArgList();
    if ( domainObjectNames.size() != 1 ) {
      printError( options, "Missing class" );
      return;
    }

    File domainSourceFile = new File( domainObjectNames.get( 0 ) );
    if ( !domainSourceFile.isFile() ) {
      printError( options, "No source file found at <" + domainSourceFile.getAbsolutePath() + ">" );
      return;
    }
    File destination = new File( commandLine.getOptionValue( OPTION_DESTINATION ) );
    if ( !destination.isDirectory() ) {
      printError( options, "Destination <" + destination.getAbsolutePath() + "> is not a directory" );
      return;
    }

    File testDestination = new File( commandLine.getOptionValue( OPTION_TEST_DESTINATION ) );
    if ( !testDestination.isDirectory() ) {
      printError( options, "Test destination <" + testDestination.getAbsolutePath() + "> is not a directory" );
      return;
    }
    GeneratorConfiguration configuration = new GeneratorConfiguration( domainSourceFile, destination, testDestination );


    File tmpDestination = createEmptyTmpDir();
    File tmpTestDestination = createEmptyTmpDir();

    GeneratorConfiguration tmpConfiguration = new GeneratorConfiguration( domainSourceFile, tmpDestination, tmpTestDestination );

    System.out.println( "Generating serializer for <" + domainSourceFile.getAbsolutePath() + ">" );
    System.out.println( "\tSerializer is created in <" + destination.getAbsolutePath() + ">" );
    System.out.println( "\tSerializer tests are created in <" + testDestination.getAbsolutePath() + ">" );


    //Now start the generator
    ClassLoader defaultClassLoader = getClass().getClassLoader();
    if ( defaultClassLoader == null ) {
      defaultClassLoader = ClassLoader.getSystemClassLoader();
    }

    ClassLoader aptClassLoader = new APTClassLoader( defaultClassLoader, getPackagePrefixes().toArray( new String[0] ) );
    Thread.currentThread().setContextClassLoader( aptClassLoader );

    Class<?> runnerType = aptClassLoader.loadClass( getRunnerClassName() );

    Object runner = Reflection.constructor().in( runnerType ).newInstance();
    Reflection.method( "generate" ).withParameterTypes( GeneratorConfiguration.class ).in( runner ).invoke( tmpConfiguration );

    System.out.println( "Generation finished!" );

    transferFiles( tmpConfiguration.getDestination(), configuration.getDestination() );
    transferFiles( tmpConfiguration.getTestDestination(), configuration.getTestDestination() );

    System.out.println( "Cleaning up..." );
    FileUtils.deleteDirectory( tmpDestination );
    FileUtils.deleteDirectory( tmpTestDestination );
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

  public interface Runner {
    void generate( @NotNull GeneratorConfiguration configuration ) throws Exception;
  }
}
