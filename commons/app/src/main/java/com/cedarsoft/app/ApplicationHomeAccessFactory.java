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

  /**
   * Creates a app home access within the temp dir
   */
  @Nonnull
  public static ApplicationHomeAccess createTemporaryApplicationHomeAccess() {
    File dir = new File(new File(System.getProperty("java.io.tmpdir")), "." + System.currentTimeMillis());
    return createTemporaryApplicationHomeAccess(dir);
  }

  @Nonnull
  public static ApplicationHomeAccess createTemporaryApplicationHomeAccess(@Nonnull File dir) {
    File configHome = new File(dir, "config");
    createDirIfNecessary(configHome);
    File data = new File(dir, "data");
    createDirIfNecessary(data);
    File cacheHome = new File(dir, "cache");
    createDirIfNecessary(cacheHome);

    return new StaticApplicationHomeAccess("mockDir", configHome, data, cacheHome);
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

    if ( !dir.mkdirs() ) {
      throw new IllegalStateException( "Could not create directory <" + dir.getAbsolutePath() + ">" );
    }
  }
}