package com.cedarsoft.matchers;

import com.google.common.io.Files;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 */
public class ContainsOnlyFilesMatcherTest {
  @Rule
  public final TemporaryFolder tmp = new TemporaryFolder();

  @Rule
  public final ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testOnly() throws IOException {
    Files.touch( new File( tmp.newFolder( "dir" ), "a" ) );
    assertThat( ContainsOnlyFilesMatcher.toTree( tmp.getRoot() ), tmp.getRoot(), ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/a" ) );

    expectedException.expect( AssertionError.class );
    Files.touch( new File( tmp.newFolder( "dir2" ), "a2" ) );
    assertThat( ContainsOnlyFilesMatcher.toTree( tmp.getRoot() ), tmp.getRoot(), ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/a" ) );
  }

  @Test
  public void testMessage() {
    Description description = new StringDescription();
    ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/a", "dir2/b" ).describeTo( description );
    assertThat( description.toString(), is( "contains only files [dir/a, dir2/b]" ) );
  }

  @Test
  public void testNone() throws IOException {
    expectedException.expect( AssertionError.class );
    assertThat( ContainsOnlyFilesMatcher.toTree( tmp.getRoot() ), tmp.getRoot(), ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/a" ) );
  }

  @Test
  public void testWrong() throws IOException {
    expectedException.expect( AssertionError.class );
    Files.touch( new File( tmp.newFolder( "dir" ), "a" ) );
    assertThat( ContainsOnlyFilesMatcher.toTree( tmp.getRoot() ), tmp.getRoot(), ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/b" ) );
  }
}
