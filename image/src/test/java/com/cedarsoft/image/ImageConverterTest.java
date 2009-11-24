package com.cedarsoft.image;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 */
public class ImageConverterTest {
  private ImageConverter imageConverter = new ImageConverter();
  private BufferedImage original;

  @BeforeMethod
  protected void setUp() throws Exception {
    original = ImageIO.read( getClass().getResource( "/paris.jpg" ) );
  }

  @Test
  public void testResize() throws InterruptedException {
    final BufferedImage resized = new ImageConverter().resize( original, new Dimension( 72, 88 ) );
    assertEquals( 72, resized.getWidth() );
    assertEquals( 88, resized.getHeight() );

    //    JFrame frame = new JFrame();
    //    frame.setContentPane( new JPanel() {
    //      @Override
    //      protected void paintComponent( Graphics g ) {
    //        super.paintComponent( g );
    //        g.drawImage( resized, 0, 0, this );
    //      }
    //    } );
    //
    //    frame.setSize( 800, 600 );
    //    frame.setVisible( true );
    //    Thread.sleep( 4000 );
  }

  @Test
  public void testSizes() throws IOException {
    assertEquals( 300, original.getWidth() );
    assertEquals( 367, original.getHeight() );

    int newWidth = original.getWidth() * 72 / 300;
    assertEquals( 72, newWidth );

    int newHeight = original.getHeight() * 72 / 300;
    assertEquals( 88, newHeight );

    Dimension dim = new ImageConverter().calculateNewDimensions( original, Resolution.DPI_300, Resolution.DPI_072 );
    assertEquals( 72, dim.width );
    assertEquals( 88, dim.height );
  }

}
