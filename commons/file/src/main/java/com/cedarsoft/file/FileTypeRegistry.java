/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.file;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * <p>FileTypeRegistry class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class FileTypeRegistry {
  /**
   * Constant <code>LIGHT_ZONE</code>
   */
  @Nonnull
  public static final FileType LIGHT_ZONE = new FileType( "LightZone", "application/lightzone", true, new Extension( "_", "lzn.jpg" ) );
  /**
   * Constant <code>JPEG</code>
   */
  @Nonnull
  public static final FileType JPEG = new FileType( "JPEG", "image/jpeg",false, new Extension( ".", "jpg" ), new Extension( ".", "jpeg" ) );
  /**
   * Constant <code>TIFF</code>
   */
  @Nonnull
  public static final FileType TIFF = new FileType( "TIFF", "image/tiff",false, new Extension( ".", "tif" ), new Extension( ".", "tiff" ) );
  /**
   * Constant <code>GIMP</code>
   */
  @Nonnull
  public static final FileType GIMP = new FileType( "Gimp", "image/xcf",false, new Extension( ".", "xcf" ) );
  /**
   * Constant <code>PHOTO_SHOP</code>
   */
  @Nonnull
  public static final FileType PHOTO_SHOP = new FileType( "Photoshop", "image/psd", false, new Extension( ".", "psd" ) );
  /**
   * Constant <code>RAW_CANON</code>
   */
  @Nonnull
  public static final FileType RAW_CANON = new FileType( "CanonRaw", "image/cr2", false, new Extension( ".", "cr2" ) );

  @Nonnull
  private static final List<FileType> DEFAULT = Arrays.asList( LIGHT_ZONE, JPEG, TIFF, GIMP, RAW_CANON, PHOTO_SHOP );
}
