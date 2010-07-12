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
import java.util.List;

/**
 * Abstract base class for generators that may be called from the CLI
 */
public abstract class AbstractCliGenerator extends AbstractGenerator {
  @NonNls
  public static final String HELP_OPTION = "h";
  @NonNls
  public static final String OPTION_DESTINATION = "d";
  @NonNls
  public static final String OPTION_TEST_DESTINATION = "t";

  protected void printError( @NotNull Options options, @NotNull @NonNls String errorMessage ) {
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

    run( domainSourceFile, destination, testDestination, System.out );
  }
}
