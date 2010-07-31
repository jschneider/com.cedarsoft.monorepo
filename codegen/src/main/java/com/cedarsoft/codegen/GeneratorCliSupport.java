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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GeneratorCliSupport {
  @NonNls
  public static final String HELP_OPTION = "h";
  @NonNls
  public static final String OPTION_DESTINATION = "d";
  @NonNls
  public static final String OPTION_TEST_DESTINATION = "t";

  @NotNull
  private final AbstractGenerator generator;
  @NotNull
  private final PrintWriter logOut;

  @NotNull
  @NonNls
  private final String commandName;

  public GeneratorCliSupport( @NotNull AbstractGenerator generator, @NotNull @NonNls String commandName ) {
    this( generator, commandName, new PrintWriter( System.out ) );
  }

  public GeneratorCliSupport( @NotNull AbstractGenerator generator, @NotNull @NonNls String commandName, @NotNull @NonNls PrintWriter logOut ) {
    this.generator = generator;
    this.logOut = logOut;
    this.commandName = commandName;
  }

  protected void printError( @NotNull Options options, @NotNull @NonNls String errorMessage ) {
    logOut.println( errorMessage );
    printHelp( options );
  }

  protected void printHelp( @NotNull Options options ) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( logOut, HelpFormatter.DEFAULT_WIDTH, commandName + " -d <serializer dest dir> -t <test dest dir> path-to-class", null, options, 0, 0, null );
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
    try {
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

      List<? extends String> domainObjectNames = commandLine.getArgList();
      if ( domainObjectNames.isEmpty() ) {
        printError( options, "Missing class" );
        return;
      }

      List<File> domainSourceFiles = new ArrayList<File>();
      for ( String domainObjectName : domainObjectNames ) {
        File domainSourceFile = new File( domainObjectName );
        if ( !domainSourceFile.isFile() ) {
          printError( options, "No source file found at <" + domainSourceFile.getAbsolutePath() + ">" );
          return;
        }
        domainSourceFiles.add( domainSourceFile );
      }

      generator.run( domainSourceFiles, destination, testDestination, logOut );
    } finally {
      logOut.flush();
      logOut.close();
    }
  }
}
