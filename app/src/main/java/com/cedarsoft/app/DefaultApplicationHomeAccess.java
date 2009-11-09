package com.cedarsoft.app;

import com.google.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.Override;

/**
 * Offers access to the application home.
 */
public class DefaultApplicationHomeAccess implements ApplicationHomeAccess {
  @NotNull
  private static final Log LOG = LogFactory.getLog( DefaultApplicationHomeAccess.class );
  @NotNull
  @NonNls
  public static final String SUFFIX_SANDBOX = "-sandbox";
  @NonNls
  private final String applicationName;
  private final File applicationHome;

  /**
   * Create a new application home access
   *
   * @param applicationName the name of the application
   * @throws IOException if an io exception occures
   */
  @Inject
  public DefaultApplicationHomeAccess( @ApplicationName @NotNull @NonNls String applicationName ) throws IOException {
    this( new File( new File( System.getProperty( "user.home" ) ), '.' + applicationName ), applicationName );
  }

  public DefaultApplicationHomeAccess( @NotNull @NonNls String applicationName, boolean sandbox ) throws IOException {
    this( new File( new File( System.getProperty( "user.home" ) ), '.' + getApplicationDirName( applicationName, sandbox ) ), applicationName );
  }

  @NotNull
  @NonNls
  public static String getApplicationDirName( @NotNull @NonNls String applicationName, boolean sandbox ) {
    if ( sandbox ) {
      return applicationName + SUFFIX_SANDBOX;
    } else {
      return applicationName;
    }
  }

  /**
   * Create a new application home access
   *
   * @param applicationHome the application home
   * @param applicationName the application name
   * @throws IOException
   */
  public DefaultApplicationHomeAccess( @NotNull File applicationHome, @NotNull @NonNls String applicationName ) throws IOException {
    if ( applicationName.length() < 3 ) {
      throw new IllegalArgumentException( "application name is too short: " + applicationName );
    }
    this.applicationHome = applicationHome;
    this.applicationName = applicationName;

    if ( !applicationHome.exists() || !applicationHome.isDirectory() ) {
      applicationHome.mkdirs();
      LOG.info( "Creating Application Directory: " + applicationHome.getAbsolutePath() );
    }
    if ( !applicationHome.canWrite() ) {
      throw new IOException( "Cannot write " + applicationHome.getAbsolutePath() );
    }
  }

  /**
   * The application name
   *
   * @return the application name
   */
  @Override
  @NotNull
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * The application home dir
   *
   * @return the application home dir
   */
  @Override
  @NotNull
  public File getApplicationHome() {
    return applicationHome;
  }
}
