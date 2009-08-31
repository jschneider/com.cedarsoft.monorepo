package com.cedarsoft.swing;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * <p/>
 * Date: Mar 16, 2007<br>
 * Time: 1:35:11 PM<br>
 */
public class ImageComponentTest {
  private ImageComponent component;

  @BeforeMethod
  protected void setUp() throws Exception {
    component = new ImageComponent();
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testDefault() {
    assertNotNull( component.getPreferredSize() );
    assertEquals( new Dimension(), component.getPreferredSize() );

    Image image = new BufferedImage( 30, 24, BufferedImage.TYPE_INT_RGB );
    component.setImage( image );

    assertNotNull( component.getPreferredSize() );
    assertEquals( new Dimension( 30, 24 ), component.getPreferredSize() );

    component.setImage( ( Image ) null );

    assertNotNull( component.getPreferredSize() );
    assertEquals( new Dimension(), component.getPreferredSize() );
  }
}
