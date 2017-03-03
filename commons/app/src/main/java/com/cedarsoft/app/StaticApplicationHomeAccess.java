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
  public File getApplicationHome() {
    throw new UnsupportedOperationException();
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
