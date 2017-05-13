package com.cedarsoft.app;

import org.junit.*;
import org.junit.rules.*;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ApplicationHomeAccessFactoryTest {
  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void name() throws Exception {
    ApplicationHomeAccess applicationHomeAccess = ApplicationHomeAccessFactory.createTemporaryApplicationHomeAccess(tmp.newFolder());

    assertThat(applicationHomeAccess.getApplicationName()).isNotNull();
    assertThat(applicationHomeAccess.getCacheHome()).isDirectory();
    assertThat(applicationHomeAccess.getConfigHome()).isDirectory();
    assertThat(applicationHomeAccess.getDataHome()).isDirectory();
  }
}