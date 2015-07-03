package com.cedarsoft.app.xdg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Created from http://standards.freedesktop.org/basedir-spec/basedir-spec-latest.html
 */
public class WindowsUtil {
  @Nonnull
  private static final File HOME_DIR = getHomeDir();

  private WindowsUtil() {
  }

  @Nonnull
  private static File getHomeDir() {
    String userHomeAsString = System.getProperty( "user.home" );
    if ( userHomeAsString == null ) {
      throw new IllegalStateException( "No property found for user.home" );
    }

    File home = new File( userHomeAsString );
    if ( !home.isDirectory() ) {
      throw new IllegalStateException( "User home <" + home.getAbsolutePath() + "> is not a directory" );
    }

    return home;
  }

  @Nonnull
  public static final String APP_DATA_HOME = "APPDATA";
  @Nonnull
  public static final String LOCAL_APP_DATA_HOME = "LOCALAPPDATA";


  /**
   * Returns the app data home dir
   */
  @Nonnull
  public static File getAppData() {
    return getDir( APP_DATA_HOME );
  }

  /**
   * Returns the local app data home dir
   */
  @Nonnull
  public static File getLocalAppData() {
    return getDir( LOCAL_APP_DATA_HOME );
  }

  @Nonnull
  private static File getDir( @Nullable String propertyName ) {
    //noinspection CallToSystemGetenv
    String dirName = System.getenv( propertyName );

    if ( dirName == null ) {
      throw new IllegalStateException( "property <" + propertyName + "> not found" );
    }
    return new File( dirName );
  }

}