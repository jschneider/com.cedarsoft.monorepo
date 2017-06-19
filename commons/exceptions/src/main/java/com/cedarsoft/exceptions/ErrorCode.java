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
package com.cedarsoft.exceptions;

import com.google.errorprone.annotations.Immutable;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Immutable
public class ErrorCode {
  private static final char SEPARATOR = '-';
  /**
   * The prefix for the error code - must be unique over all components
   */
  @Nonnull
  private final Prefix prefix;
  /**
   * The id for this error code - must be unique for one prefix
   */
  private final int id;

  /**
   * Creates a new error code
   *
   * @param prefix the prefix
   * @param id     the id
   */
  public ErrorCode(@Nonnull Prefix prefix, int id) {
    this.prefix = prefix;
    this.id = id;
  }

  /**
   * Returns the prefix of the error code
   *
   * @return the prefix of the error code
   */
  @Nonnull
  public Prefix getPrefix() {
    return prefix;
  }

  /**
   * Returns the id
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the error code formatted as string
   *
   * @return the error code as string
   */
  @Nonnull
  public String format() {
    return prefix.asString() + SEPARATOR + id;
  }

  @Override
  public String toString() {
    return format();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ErrorCode)) {
      return false;
    }

    ErrorCode errorCode = (ErrorCode) o;

    if (id != errorCode.id) {
      return false;
    }
    if (prefix != null ? !prefix.equals(errorCode.prefix) : errorCode.prefix != null) {
      return false;
    }

    return true;
  }

  //CHECKSTYLE:OFF
  @Override
  public int hashCode() {
    int result = prefix != null ? prefix.hashCode() : 0;
    result = 31 * result + id;
    return result;
  }
  //CHECKSTYLE:ON

  /**
   * Represents a prefix for error codes.
   * The prefix is used to structure the error codes.
   * The ids are unique within a prefix.
   */
  @Immutable
  public static class Prefix {
    @Nonnull
    private final String prefix;

    /**
     * Creates a new prefix
     *
     * @param prefix the prefix
     */
    public Prefix(@Nonnull String prefix) {
      this.prefix = prefix;
    }

    /**
     * Returns the string representation of this prefix
     *
     * @return the string representation
     */
    @Nonnull
    public String asString() {
      return prefix;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Prefix)) {
        return false;
      }

      Prefix prefix1 = (Prefix) o;

      if (prefix != null ? !prefix.equals(prefix1.prefix) : prefix1.prefix != null) {
        return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return prefix != null ? prefix.hashCode() : 0;
    }

    @Override
    public String toString() {
      return prefix;
    }
  }
}
