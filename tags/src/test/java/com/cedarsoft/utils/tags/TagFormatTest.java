package com.cedarsoft.utils.tags;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.Arrays;

/**
 *
 */
public class TagFormatTest {
  @Test
  public void testSimple() {
    Tag tag0 = new Tag( "A" );
    Tag tag1 = new Tag( "B" );
    Tag tag2 = new Tag( "C" );

    assertEquals( "A, B, C", TagFormat.getSimple().formatTags( Arrays.asList( tag0, tag1, tag2 ) ) );
  }
}
