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
package it.neckar.open.serialization.neo4j.test.utils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * The type Person.
 */
public class Person {
  @Nonnull
  private final String name;
  @Nonnull
  private final Address address;
  @Nonnull
  private final List<? extends Email> mails;

  /**
   * Instantiates a new Person.
   *
   * @param name the name
   * @param address the address
   * @param mails the mails
   */
  public Person( @Nonnull String name, @Nonnull Address address, @Nonnull List<? extends Email> mails ) {
    this.name = name;
    this.address = address;
    this.mails = mails;
  }

  /**
   * Gets mails.
   *
   * @return the mails
   */
  @Nonnull
  public List<? extends Email> getMails() {
    return mails;
  }

  /**
   * Gets address.
   *
   * @return the address
   */
  @Nonnull
  public Address getAddress() {
    return address;
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  @Nonnull
  public String getName() {
    return name;
  }
}
