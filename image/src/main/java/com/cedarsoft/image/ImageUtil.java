/*
* Copyright (c) 2002-2003, William Denniss
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright notice,
*       this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the Tank Software nor the names of its
*       contributors may be used to endorse or promote products derived from
*       this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
* THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.cedarsoft.image;

import javax.imageio.ImageIO;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Image manipulation methods.
 *
 * @author William Denniss
 * @version 2.4, 22th October 2003
 */
public class ImageUtil {

  /**
   * Returns a cropped instance of the image.  If null is passed as either of the corners
   * then nothing is cropped.
   *
   * @param in     The source image
   * @param top    The top left hand corner of the crop rectangle
   * @param bottom The bottom right hand corner of the crop rectangle
   * @return the cropped instance of the image
   */
  public static BufferedImage cropImage( BufferedImage in, Point top, Point bottom ) {

    // If the crop triangle is null, then no crop is performed
    if ( top == null || bottom == null ) {
      return in;
    }

    // Calculates the size of the final image
    Dimension size = new Dimension( bottom.x - top.x, bottom.y - top.y );

    // Creates a new image to that size
    BufferedImage cropped = new BufferedImage( size.width, size.height, BufferedImage.TYPE_INT_RGB );

    // Performes the crop
    cropped.createGraphics().drawImage( in, 0, 0, size.width, size.height, top.x, top.y, bottom.x, bottom.y, null );

    return cropped;

  }

  /**
   * Reads an image from the disk as a BufferedImage - can be case as an Image
   *
   * @param filename the image to be read
   * @return the image object
   */
  public static BufferedImage readImage( String filename ) throws IOException {
    File imagein = new File( filename );
    BufferedImage bi = ImageIO.read( imagein );
    return bi;
  }

  /**
   * Loades the passed image into memory.
   *
   * @param toLoad the image that will be loaded into memory
   * @param c      the Component to be notified
   * @param timout the number of milliseconds before loading will be aborted
   * @return boolen representing success of operation
   */
  public static boolean loadImageNow( Image toLoad, Component c, int timeout ) {
    MediaTracker tracker = new MediaTracker( c );
    tracker.addImage( toLoad, 0 );
    try {
      tracker.waitForAll( timeout );
      return true;
    } catch ( InterruptedException e ) {
      return false;
    }
  }


  /**
   * Scales an Image.
   * <p/>
   * Useful ref: 	http://saloon.javaranch.com/cgi-bin/ubb/ultimatebb.cgi?ubb=get_topic&f=34&t=002153
   */

  public static BufferedImage scaleImage( BufferedImage imageIn, int width, int height ) throws IOException {
    return scaleImage( imageIn, width, height, Image.SCALE_SMOOTH, false );
  }

  public static BufferedImage scaleImage( BufferedImage imageIn, int width, int height, int scaleQuality ) throws IOException {
    return scaleImage( imageIn, width, height, scaleQuality, false );

  }

  public static BufferedImage scaleImage( BufferedImage imageIn, int width, int height, int scaleQuality, boolean maintainAspect ) throws IOException {


    if ( maintainAspect && width != -1 && height != -1 ) {

      // Calculate the height and width - this is done automatically if not constrained
      int im_width = imageIn.getWidth( null );
      int im_height = imageIn.getHeight( null );

      int tmp_h = height;
      int tmp_w = ( int ) ( ( ( double ) height / ( double ) im_height ) * im_width );

      if ( tmp_w > width ) {
        tmp_w = width;
        tmp_h = ( int ) ( ( ( double ) width / ( double ) im_width ) * im_height );
      }

      width = tmp_w;
      height = tmp_h;
    }


    // Scales the image
    Image imageIn2 = imageIn.getScaledInstance( width, height, scaleQuality );

    // Converts the Image to a BufferedImage
    BufferedImage scaled = new BufferedImage( imageIn2.getWidth( null ), imageIn2.getHeight( null ), BufferedImage.TYPE_INT_RGB );
    scaled.createGraphics().drawImage( imageIn2, 0, 0, null );

    return scaled;
  }

  /**
   * Writes a buffered Image to disk
   */
  public static void writeImage( BufferedImage toWrite, String fileout ) throws IOException {
    // The output file
    File f = new File( fileout );

    // Gets the output format based on the file extention
    String ext = fileout.substring( fileout.length() - 3, fileout.length() );
    String filetype = "jpeg";
    if ( ext.equalsIgnoreCase( "jpg" ) ) {
      filetype = "jpeg";
    } else if ( ext.equalsIgnoreCase( "gif" ) ) {
      filetype = "gif";
    } else if ( ext.equalsIgnoreCase( "png" ) ) {
      filetype = "png";
    }

    // Writes the file
    ImageIO.write( toWrite, filetype, f );

  }

  public static BufferedImage rotateImage( BufferedImage bi, int rotations ) {
    rotations %= 4;

    int newWidth = bi.getWidth();
    int newHeight = bi.getHeight();


    // calculates the new image dimentions
    if ( rotations % 2 != 0 ) {
      newHeight = bi.getWidth();
      newWidth = bi.getHeight();
    }

    // calculates the rotated image offset
    int moveY = 0;
    if ( rotations > 1 ) {
      moveY = newHeight;
    }
    int moveX = 0;
    if ( rotations > 0 && rotations < 3 ) {
      moveX = newWidth;
    }

    // Creates the new image and canvas
    BufferedImage rbi = new BufferedImage( newWidth, newHeight, BufferedImage.TYPE_INT_RGB );
    Graphics2D g2d = rbi.createGraphics();

    // setups the rotation
    AffineTransform af = new AffineTransform();
    af.concatenate( AffineTransform.getTranslateInstance( moveX, moveY ) );
    af.concatenate( AffineTransform.getRotateInstance( ( rotations * 0.5 ) * Math.PI ) );

    // adds the image with rotation
    g2d.drawImage( ( Image ) bi, af, null );


    return rbi;
  }
}
