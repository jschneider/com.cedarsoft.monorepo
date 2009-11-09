package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.swing.JFrame;
import java.util.Collections;
import java.util.List;

/**
 * <p/>
 * Date: Apr 23, 2007<br>
 * Time: 1:06:08 PM<br>
 */
public class ChildDetectorManagerTest {
  private ChildDetectorManager manager;

  @BeforeMethod
  protected void setUp() throws Exception {
    manager = new ChildDetectorManager();
    manager.addChildDetector( String.class, new AbstractChildDetector<String, String>() {
      @java.lang.Override
      @NotNull
      public List<? extends String> findChildren( @NotNull String parent ) {
        return Collections.emptyList();
      }
    } );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testIt() {
    assertNotNull( manager.getChildDetector( String.class ) );
    try {
      manager.getChildDetector( JFrame.class );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }
}
