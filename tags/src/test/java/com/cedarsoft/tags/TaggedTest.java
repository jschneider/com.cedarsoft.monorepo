package com.cedarsoft.tags;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.Collection;

/**
 * <p/>
 * Date: Apr 2, 2007<br>
 * Time: 1:55:28 PM<br>
 */
public class TaggedTest {
  private TaggedObject object;

  @BeforeMethod
  protected void setUp() throws Exception {
    object = new TaggedObject();
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testIt() {
    Collection<? extends Tag> tags = object.getTags();
    assertNotNull( tags );
    assertEquals( 0, tags.size() );
  }

  @Test
  public void testAdd() {
    object.setTags( new Tag( "1" ), new Tag( "2" ), new Tag( "3" ) );
    assertEquals( 3, object.getTags().size() );
  }
}
