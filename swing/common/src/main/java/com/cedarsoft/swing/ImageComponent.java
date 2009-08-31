package com.cedarsoft.swing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The image component is able to render images
 */
public class ImageComponent extends JComponent {
  private List<Painter> painters = new ArrayList<Painter>();
  private Image image;
  private Dimension imageSize;

  @Override
  protected void paintComponent( @NotNull Graphics g ) {
    super.paintComponent( g );
    if ( image != null )
      g.drawImage( image, 0, 0, this );

    if ( !painters.isEmpty() ) {
      for ( Painter painter : painters ) {
        painter.paint( g, this );
      }
    }
  }

  /**
   * Adds a custom paointer
   *
   * @param painter the painter
   */
  public void addPainter( @NotNull Painter painter ) {
    painters.add( painter );
  }

  @Override
  public Dimension getPreferredSize() {
    if ( isPreferredSizeSet() ) {
      return super.getPreferredSize();
    }
    if ( imageSize != null ) {
      return imageSize;
    }
    return super.getPreferredSize();
  }

  /**
   * Sets the image
   *
   * @param imageFile the image file
   * @throws IOException
   */
  public void setImage( @NotNull File imageFile ) throws IOException {
    setImage( ImageIO.read( imageFile ) );
  }

  /**
   * Sets the image
   *
   * @param image the image
   */
  public void setImage( @Nullable Image image ) {
    this.image = image;
    if ( image == null ) {
      imageSize = null;
    } else {
      int width = image.getWidth( this );
      int height = image.getHeight( this );
      this.imageSize = new Dimension( width, height );
    }
    revalidate();
    repaint();
  }

  /**
   * Extends the painting of the ImageComponent
   */
  public interface Painter {
    /**
     * Paint the custom painting
     *
     * @param g         the context
     * @param component the component
     */
    void paint( @NotNull Graphics g, @NotNull JComponent component );
  }
}