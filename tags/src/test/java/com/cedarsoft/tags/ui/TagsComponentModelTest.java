package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.DefaultTagManager;
import com.cedarsoft.tags.TagManager;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: May 3, 2007<br>
 * Time: 5:05:09 PM<br>
 */
public class TagsComponentModelTest {
  private TagManager<Object> tagManager;
  private TagsComponentModel model;

  @BeforeMethod
  protected void setUp() throws Exception {
    tagManager = new DefaultTagManager<Object>();
    model = new DefaultTagsComponentModel( tagManager, tagManager.getTaggable( "asf" ) );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testIt() {
    assertNotNull( model.getTagProvider() );
    assertNotNull( model.getSelectedTags() );

    tagManager.getTag( "a" );
    tagManager.getTag( "b" );
    tagManager.getTag( "c" );

    assertEquals( 3, model.getTagProvider().getTags().size() );
    assertEquals( 0, model.getSelectedTags().getTags().size() );

    model.selectTag( model.getTagProvider().getTags().get( 0 ) );
    assertEquals( 3, model.getTagProvider().getTags().size() );
    assertEquals( 1, model.getSelectedTags().getTags().size() );
  }
}
