package com.cedarsoft.convert;

import org.testng.annotations.*;

import java.awt.Point;
import java.awt.Rectangle;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: 13.11.2006<br>
 * Time: 17:00:54<br>
 */
public class StringConverterTests {
  private StringConverterManager converterManager;

  @BeforeMethod
  protected void setUp() throws Exception {
    converterManager = new StringConverterManager( true );
  }

  @Test
  public void testBounds() {
    StringConverter<Rectangle> converter = converterManager.findConverter( Rectangle.class );
    assertNotNull( converter );

    Rectangle created = converter.createObject( converter.createRepresentation( new Rectangle( 10, 20, 30, 40 ) ) );
    assertEquals( 10, created.x );
    assertEquals( 20, created.y );
    assertEquals( 30, created.width );
    assertEquals( 40, created.height );
  }

  @Test
  public void testPoint() {
    StringConverter<Point> converter = converterManager.findConverter( Point.class );
    assertNotNull( converter );

    Point created = converter.createObject( converter.createRepresentation( new Point( 10, 20 ) ) );
    assertEquals( 10, created.x );
    assertEquals( 20, created.y );
  }
}
