package com.cedarsoft.utils;

import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 *
 */
@Deprecated
@Singleton
public class MockApplicationHomeAccess implements ApplicationHomeAccess {
  public static final String APPNAME = "mockAppHome";

  @NotNull
  private final File projectHome;

  public MockApplicationHomeAccess() {
    projectHome = new File( new File( System.getProperty( "java.io.tmpdir" ) ), "." + APPNAME );
    try {
      FileUtils.deleteDirectory( projectHome );
    } catch ( IOException ignore ) {
    }

    if ( !projectHome.mkdirs() ) {
      throw new IllegalStateException( "Could not create " + projectHome.getAbsolutePath() );
    }

    assert projectHome.exists();
  }

  @NotNull
  public String getApplicationName() {
    return APPNAME;
  }

  @NotNull
  public File getApplicationHome() {
    return projectHome;
  }
}


