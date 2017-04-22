package com.cedarsoft.osgi.validator;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Validates the package structure for usage with OSGi.
 * This plugin verifies whether the groupId and artifactId are reflected by the package names.
 * This ensures that no duplicate packages can be exported.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Mojo( name = "validate", defaultPhase = LifecyclePhase.VALIDATE )
public class ValidatorMojo extends SourceFolderAwareMojo {
  public static final String MAVEN_PLUGIN_SUFFIX = "-maven-plugin";
  /**
   * Whether the build shall fail if a validation is detected
   */
  @Parameter( defaultValue = "${fail}", property = "osgi-validation.fail" )
  private boolean fail = true;

  /**
   * The source directories containing the test sources to be compiled.
   */
  @Parameter( defaultValue = "${skipped.files}", property = "skipped.files" )
  protected List<String> skippedFiles = new ArrayList<String>();

  /**
   * The prohibited package parts
   */
  @Parameter
  protected Set<String> prohibitedPackages = ImmutableSet.of( "internal" );

  @Parameter
  protected Set<String> packagePartsToSkip = ImmutableSet.of( "commons", "maven", "plugin" );

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if ( "pom".equals( mavenProject.getPackaging() ) ) {
      getLog().info( "Skipping for packaging \"pom\"" );
      return;
    }

    getLog().info( "Validating OSGi-stuff" );

    validatePackages();
    validateImportedPackages();
  }

  private void validateImportedPackages() throws MojoFailureException {
    File manifestFile = new File( new File( classesDir, "META-INF" ), "MANIFEST.MF" );

    if ( !manifestFile.exists() ) {
      getLog().info( "No MANIFEST.MF found" );
      return;
    }

    getLog().info( "Validating MANIFEST @ " + manifestFile.getAbsolutePath() );

    try {
      FileInputStream is = new FileInputStream( manifestFile );
      try {
        Manifest manifest = new Manifest( is );


        boolean containsError = false;
        Attributes mainAttributes = manifest.getMainAttributes();

        @Nullable String exportPackage = mainAttributes.getValue( "Export-Package" );
        if ( exportPackage != null ) {
          Iterable<String> packages = Splitter.on( ',' ).split( exportPackage );
          for ( String packageName : packages ) {
            for ( String prohibitedPackage : prohibitedPackages ) {
              if ( packageName.contains( prohibitedPackage ) ) {
                getLog().error( "Prohibited package exported: " + packageName );
                containsError = true;
              }
            }
          }
        }

        @Nullable String importPackage = mainAttributes.getValue( "Import-Package" );
        if ( importPackage != null ) {
          Iterable<String> packages = Splitter.on( ',' ).split( importPackage );
          for ( String packageName : packages ) {
            for ( String prohibitedPackage : prohibitedPackages ) {
              if ( packageName.contains( prohibitedPackage ) ) {
                getLog().error( "Prohibited package imported: " + packageName );
                containsError = true;
              }
            }
          }
        }

        if ( containsError ) {
          throw new MojoFailureException( "Invalid package export/import" );
        }
      } finally {
        is.close();
      }
    } catch ( IOException e ) {
      throw new MojoFailureException( "Could not read manifest <" + manifestFile + ">", e );
    }
  }

  private void validatePackages() throws MojoExecutionException {
    Map<String, ValidationFailedException> problematicFiles = new TreeMap<String, ValidationFailedException>();

    getLog().info( "Source Roots:" );
    getLog().debug( "Skipped Files: " + skippedFiles );

    for ( String sourceRoot : getSourceRoots() ) {
      getLog().info( "\t" + sourceRoot );

      File sourceRootDir = new File( sourceRoot );

      if ( !sourceRootDir.isDirectory() ) {
        getLog().info( "Skipping <" + sourceRoot + ">: Is not a directory." );
        continue;
      }

      problematicFiles.putAll( validate( sourceRootDir, skippedFiles ) );
    }

    if ( problematicFiles.isEmpty() ) {
      getLog().info( "No problematic files found" );
      return;
    }

    if ( fail ) {
      getLog().error( "Found files within a problematic package:" );
      for ( Map.Entry<String, ValidationFailedException> entry : problematicFiles.entrySet() ) {
        getLog().error( "  " + entry.getKey() );
        //noinspection ThrowableResultOfMethodCallIgnored
        getLog().error( "     " + entry.getValue().getMessage() );
      }
      throw new MojoExecutionException( "There exist " + problematicFiles.size() + " files that seem to be placed within a problematic package" );
    } else {
      getLog().warn( "Found files within a problematic package:" );
      for ( Map.Entry<String, ValidationFailedException> entry : problematicFiles.entrySet() ) {
        getLog().warn( "  " + entry.getKey() );
        //noinspection ThrowableResultOfMethodCallIgnored
        getLog().warn( "     " + entry.getValue().getMessage() );
      }
    }
  }

  @Nonnull
  private Map<String, ValidationFailedException> validate( @Nonnull File sourceRoot, @Nonnull Collection<? extends String> skippedFiles ) {
    String[] javaFiles = findAllJavaFiles( sourceRoot, skippedFiles );

    String groupId = getProject().getGroupId();
    String artifactId = getProject().getArtifactId();
    String projectId = groupId + "." + artifactId;

    Validator validator = new Validator( projectId, packagePartsToSkip );

    Map<String, ValidationFailedException> problematicFiles = new TreeMap<String, ValidationFailedException>();
    for ( String javaFile : javaFiles ) {
      getLog().debug( "\tvalidating " + javaFile );

      try {
        validator.isValid( javaFile );
      } catch ( ValidationFailedException e ) {
        //noinspection ThrowableResultOfMethodCallIgnored
        problematicFiles.put( javaFile, e );
      }
    }

    return problematicFiles;
  }

  @Nonnull
  private static String[] findAllJavaFiles( @Nonnull File sourceRoot, @Nonnull Collection<? extends String> skippedFiles ) {
    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setBasedir( sourceRoot );
    scanner.setIncludes( new String[]{"**/*.java"} );

    Set<String> excludes = Sets.newHashSet();
    excludes.addAll( skippedFiles );

    scanner.setExcludes( excludes.toArray( new String[excludes.size()] ) );
    scanner.scan();

    return scanner.getIncludedFiles();
  }

  @Deprecated
  @Nullable
  private static String skip( @Nonnull String id, @Nonnull String toSkip ) {
    if ( !id.contains( toSkip ) ) {
      return null;
    }

    return id.replace( toSkip, "" );
  }
}
