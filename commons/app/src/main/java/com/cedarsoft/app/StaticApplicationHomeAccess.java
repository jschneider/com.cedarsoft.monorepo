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

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Simple implementation that just holds the dirs as fields
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class StaticApplicationHomeAccess implements ApplicationHomeAccess {
  @Nonnull
  private final String applicationName;
  @Nonnull
  private final File configHome;
  @Nonnull
  private final File dataHome;
  @Nonnull
  private final File cacheHome;

  public StaticApplicationHomeAccess( @Nonnull String applicationName, @Nonnull File configHome, @Nonnull File dataHome, @Nonnull File cacheHome ) {
    this.applicationName = applicationName;
    this.configHome = configHome;
    this.dataHome = dataHome;
    this.cacheHome = cacheHome;
  }

  @Nonnull
  @Override
  public String getApplicationName() {
    return applicationName;
  }

  @Nonnull
  @Override
  public File getConfigHome() {
    return configHome;
  }

  @Nonnull
  @Override
  public File getDataHome() {
    return dataHome;
  }

  @Nonnull
  @Override
  public File getCacheHome() {
    return cacheHome;
  }
}
