package com.cedarsoft.io;

import com.cedarsoft.TestUtils;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class LinkUtilsTest {
  @Test
  public void testCreation() throws IOException {
    File target = File.createTempFile( "asdf", "linked.to" );
    target.createNewFile();

    assertFalse( LinkUtils.isLink( target ) );

    File dir = TestUtils.createEmptyTmpDir();
    File link = new File( dir, "link" );
    assertFalse( link.exists() );

    LinkUtils.createSymbolicLink( target, link );
    assertTrue( link.exists() );
    assertTrue( LinkUtils.isLink( link ) );

    assertTrue( target.exists() );
    LinkUtils.deleteSymbolicLink( link );
    assertTrue( target.exists() );
    assertFalse( link.exists() );
  }
}
