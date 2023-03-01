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
package com.cedarsoft.photos;

import it.neckar.open.crypt.Algorithm;
import it.neckar.open.crypt.Hash;

import javax.annotation.Nonnull;

/**
 * Represents a split hash
 */
public class SplitHash {
  public static final int FIRST_PART_LENGTH = 2;

  @Nonnull
  private final String firstPart;
  @Nonnull
  private final String leftover;

  public SplitHash(@Nonnull String firstPart, @Nonnull String leftover) {
    this.firstPart = firstPart;
    this.leftover = leftover;
  }

  @Nonnull
  public String getFirstPart() {
    return firstPart;
  }

  @Nonnull
  public String getLeftover() {
    return leftover;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    SplitHash splitHash = (SplitHash) obj;

    if (!firstPart.equals(splitHash.firstPart)) {
      return false;
    }
    return leftover.equals(splitHash.leftover);

  }

  @Override
  public int hashCode() {
    int result = firstPart.hashCode();
    result = 31 * result + leftover.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "SplitHash{" + firstPart + ':' + leftover + '}';
  }

  @Nonnull
  public static SplitHash split(@Nonnull Hash hash) {
    return split(hash.getValueAsHex());
  }

  @Nonnull
  public static SplitHash split(@Nonnull String hash) {
    if (hash.length() <= FIRST_PART_LENGTH) {
      throw new IllegalArgumentException("Id <" + hash + "> too short. Min length is 3");
    }

    return new SplitHash(hash.substring(0, FIRST_PART_LENGTH), hash.substring(FIRST_PART_LENGTH));
  }

  @Nonnull
  public Hash toHash(@Nonnull Algorithm algorithm) {
    return Hash.fromHex(algorithm, getHashAsHex());
  }

  /**
   * Returns the complete hash (first part + leftover)
   */
  @Nonnull
  public String getHashAsHex() {
    return firstPart + leftover;
  }
}
