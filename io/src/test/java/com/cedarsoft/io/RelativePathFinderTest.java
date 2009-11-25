package com.cedarsoft.io;

import org.testng.annotations.*;

import java.io.File;

import static org.testng.Assert.*;

/**
 *
 */
public class RelativePathFinderTest {
  @Test
  public void testBug() {
    assertEquals( RelativePathFinder.getRelativePath( "/media/tamar/data/fotos/collustra/repository/10/6d775ae5eda36d969c9ab21068a32803b2ebe96b2ad581ab4e84fe0cc9d34b/data.CR2", "/media/tamar/data/fotos/collustra/links/by-date/UTC/2009/05/20", "/" ), "../../../../../../repository/10/6d775ae5eda36d969c9ab21068a32803b2ebe96b2ad581ab4e84fe0cc9d34b/data.CR2" );
  }

  @Test
  public void testGetRelativePath() {
    assertEquals( RelativePathFinder.getRelativePath( "/tmp/a", "/tmp/other/a/b", "/" ), "../../../a" );
    assertEquals( RelativePathFinder.getRelativePath( "/tmp/referenced/", "/tmp/356406/a/b", "/" ), "../../../referenced" );
    assertEquals( RelativePathFinder.getRelativePath( "/tmp/referenced", "/tmp/356406/a/b", "/" ), "../../../referenced" );
  }

  @Test
  public void testGetRelativePath3() {
    assertEquals( RelativePathFinder.getRelativePath( "/a", "/a", "/" ), "." );
    assertEquals( RelativePathFinder.getRelativePath( "/a", "/a/b", "/" ), ".." );
    assertEquals( RelativePathFinder.getRelativePath( "/a", "/a/b/c", "/" ), "../.." );
    assertEquals( RelativePathFinder.getRelativePath( "/a", "/a/b/c/d", "/" ), "../../.." );


    assertEquals( RelativePathFinder.getRelativePath( "/a", "/other/a/b", "/" ), "../../../a" );
  }

  @Test
  public void testGetRelativePath2() {
    assertEquals( RelativePathFinder.getRelativePath( "/a/b/c", "/a/x/y/", "/" ), "../../b/c" );
    assertEquals( RelativePathFinder.getRelativePath( "/a/b/", "/a/x/y/", "/" ), "../../b" );
    assertEquals( RelativePathFinder.getRelativePath( "/a/", "/a/x/y/", "/" ), "../.." );
  }

  @Test
  public void testDirectParent() {
    assertEquals( RelativePathFinder.getRelativePath( "/var/data/stuff/xyz.dat", "/var/data/", "/" ), "stuff/xyz.dat" );
    assertEquals( RelativePathFinder.getRelativePath( "/var/data/stuff/xyz.dat/", "/var/data/", "/" ), "stuff/xyz.dat" );
    assertEquals( RelativePathFinder.getRelativePath( "/var/data/stuff/xyz.dat/", "/var/data", "/" ), "stuff/xyz.dat" );
    assertEquals( RelativePathFinder.getRelativePath( "/var/data/stuff/xyz.dat", "/var/data", "/" ), "stuff/xyz.dat" );
  }

  @Test
  public void testFile() {
    assertEquals( RelativePathFinder.getRelativePath( new File( "/var/data/stuff/xyz.dat" ), new File( "/var/data/" ), "/" ).getPath(), "stuff/xyz.dat" );
    assertEquals( RelativePathFinder.getRelativePath( new File( "/a/b/c" ), new File( "/a/x/y/" ), "/" ).getPath(), "../../b/c" );
  }
}
