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