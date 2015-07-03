package com.cedarsoft.app;

import com.cedarsoft.app.xdg.WindowsUtil;
import com.cedarsoft.app.xdg.XdgUtil;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ApplicationHomeAccessFactory {
  @Nonnull
  public static ApplicationHomeAccess create( @Nonnull String applicationName ) {
    String osName = System.getProperty( "os.name" );
    if ( osName == null ) {
      throw new IllegalStateException( "Property os.name not found" );
    }

    if ( osName.contains( "Linux" ) ) {
      return createLinuxHomeAccess( applicationName );
    }

    if ( osName.contains( "Windows" ) ) {
      return createWindowsHomeAccess( applicationName );
    }

    throw new IllegalStateException( "Unsupported OS: " + osName );
  }

  @Nonnull
  private static ApplicationHomeAccess createWindowsHomeAccess( @Nonnull String applicationName ) {
    File appData = new File( WindowsUtil.getAppData(), applicationName );
    createDirIfNecessary( appData );
    File localAppData = new File( WindowsUtil.getLocalAppData(), applicationName );
    createDirIfNecessary( localAppData );
    return new StaticApplicationHomeAccess( applicationName, appData, appData, localAppData );
  }

  @Nonnull
  private static ApplicationHomeAccess createLinuxHomeAccess( @Nonnull String applicationName ) {
    File configHome = new File( XdgUtil.getConfigHome(), applicationName );
    createDirIfNecessary( configHome );
    File dataHome = new File( XdgUtil.getDataHome(), applicationName );
    createDirIfNecessary( dataHome );
    File cacheHome = new File( XdgUtil.getCacheHome(), applicationName );
    createDirIfNecessary( cacheHome );

    return new StaticApplicationHomeAccess( applicationName, configHome, dataHome, cacheHome );
  }

  private static void createDirIfNecessary( @Nonnull File dir ) {
    if ( dir.isDirectory() ) {
      return;
    }

    if ( dir.isFile() ) {
      throw new IllegalStateException( dir.getAbsolutePath() + " is a file" );
    }

    if ( dir.exists() ) {
      throw new IllegalStateException( dir.getAbsolutePath() + " still exists but is not a dir" );
    }

    if ( !dir.mkdir() ) {
      throw new IllegalStateException( "Could not create directory <" + dir.getAbsolutePath() + ">" );
    }
  }
}