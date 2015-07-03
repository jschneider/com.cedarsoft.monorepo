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
    File localAppData = new File( WindowsUtil.getLocalAppData(), applicationName );
    return new StaticApplicationHomeAccess( applicationName, appData, appData, localAppData );
  }

  @Nonnull
  private static ApplicationHomeAccess createLinuxHomeAccess( @Nonnull String applicationName ) {
    return new StaticApplicationHomeAccess( applicationName, new File( XdgUtil.getConfigHome(), applicationName ), new File( XdgUtil.getDataHome(), applicationName ), new File( XdgUtil.getCacheHome(), applicationName ) );
  }
}