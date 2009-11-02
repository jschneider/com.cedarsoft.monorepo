package com.cedarsoft.app;

import com.google.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Offers access to the application home.
 */
public class DefaultApplicationHomeAccess implements ApplicationHomeAccess {
  private static final Log log = LogFactory.getLog( DefaultApplicationHomeAccess.class );

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
      log.info( "Creating Application Directory: " + applicationHome.getAbsolutePath() );
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
  @NotNull
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * The application home dir
   *
   * @return the application home dir
   */
  @NotNull
  public File getApplicationHome() {
    return applicationHome;
  }
}
