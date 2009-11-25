package com.cedarsoft.app;

import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 *
 */
@Deprecated
@Singleton
public class MockApplicationHomeAccess implements ApplicationHomeAccess {
  @NotNull
  @NonNls
  public static final String APP_NAME = "mockAppHome";
  @NotNull
  @NonNls
  public static final String SKIP_DELETION = "skipDeletion";

  @NotNull
  private final File projectHome;

  public MockApplicationHomeAccess() {
    projectHome = new File( new File( System.getProperty( "java.io.tmpdir" ) ), "." + APP_NAME );

    try {
      if ( !Boolean.parseBoolean( System.getProperty( SKIP_DELETION ) ) ) {
        FileUtils.deleteDirectory( projectHome );
      }
    } catch ( IOException ignore ) {
    }

    projectHome.mkdirs();

    if ( !projectHome.isDirectory() ) {
      throw new IllegalStateException( "Could not create " + projectHome.getAbsolutePath() );
    }

    assert projectHome.exists();
  }

  @Override
  @NotNull
  public String getApplicationName() {
    return APP_NAME;
  }

  @Override
  @NotNull
  public File getApplicationHome() {
    return projectHome;
  }
}


