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

package com.cedarsoft.crypt;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Calculates the hashes of resources
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
@Deprecated
public class ResourceHashCalculator {
  /**
   * <p>calculate</p>
   *
   * @param algorithm a {@link com.cedarsoft.crypt.Algorithm} object.
   * @param resource a {@link java.net.URL} object.
   * @return a {@link com.cedarsoft.crypt.Hash} object.
   * @throws java.io.IOException if any.
   */
  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull URL resource ) throws IOException {
    return HashCalculator.calculate( algorithm.getMessageDigest(), resource );
  }

  /**
   * <p>calculate</p>
   *
   * @param algorithm a {@link com.cedarsoft.crypt.Algorithm} object.
   * @param resource a {@link java.io.InputStream} object.
   * @return a {@link com.cedarsoft.crypt.Hash} object.
   * @throws java.io.IOException if any.
   */
  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull InputStream resource ) throws IOException {
    return HashCalculator.calculate( algorithm.getMessageDigest(), resource );
  }

  /**
   * <p>calculate</p>
   *
   * @param algorithm a {@link com.cedarsoft.crypt.Algorithm} object.
   * @param resource an array of byte.
   * @return a {@link com.cedarsoft.crypt.Hash} object.
   * @throws java.io.IOException if any.
   */
  @NotNull
  public Hash calculate( @NotNull Algorithm algorithm, @NotNull byte[] resource ) throws IOException {
    return HashCalculator.calculate( algorithm.getMessageDigest(), new ByteArrayInputStream( resource ) );
  }

  /**
   * <p>calculateSHA1</p>
   *
   * @param resource a {@link java.io.InputStream} object.
   * @return a {@link com.cedarsoft.crypt.Hash} object.
   * @throws java.io.IOException if any.
   */
  @Deprecated
  @NotNull
  public Hash calculateSHA1( @NotNull InputStream resource ) throws IOException {
    return calculate( Algorithm.SHA1, resource );
  }

  /**
   * <p>calculateMD5</p>
   *
   * @param resource a {@link java.io.InputStream} object.
   * @return a {@link com.cedarsoft.crypt.Hash} object.
   * @throws java.io.IOException if any.
   */
  @Deprecated
  @NotNull
  public Hash calculateMD5( @NotNull InputStream resource ) throws IOException {
    return calculate( Algorithm.MD5, resource );
  }
}
