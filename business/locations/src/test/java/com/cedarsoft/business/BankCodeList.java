/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.business;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import com.cedarsoft.commons.Strings;
import com.cedarsoft.registry.cache.Cache;
import com.cedarsoft.registry.cache.HashedCache;

/**
 *
 */
public class BankCodeList extends AbstractCodesProvider {

  private static final String RESOURCE_NAME_DE_BANK_CODES = "de_bankCodes.csv";

  public BankCodeList() throws IOException {
    URL resource = getClass().getResource(RESOURCE_NAME_DE_BANK_CODES);
    if (resource == null) {
      throw new IOException("Resource \"" + RESOURCE_NAME_DE_BANK_CODES + "\" not found");
    }

    LineIterator iterator = IOUtils.lineIterator(resource.openStream(), "UTF-8");
    iterator.nextLine();

    while (iterator.hasNext()) {
      String line = iterator.nextLine();
      String[] parts = line.split("\t");
      if (parts.length < 2) {
        continue;
      }

      String code = parts[0];
      String name = Strings.stripQuotes(parts[5]);

      if (code.isEmpty()) {
        throw new IllegalStateException("Invalid code: " + name);
      }
      if (name.isEmpty()) {
        throw new IllegalStateException("Invalid name " + code);
      }

      code2names.put(code, name);
      name2codes.put(name, code);
    }
  }

  @Nonnull

  public String findDescription(@Nonnull String bankCode) {
    Collection<String> names = getNames(bankCode);
    if (names.isEmpty()) {
      throw new IllegalArgumentException("No name found for " + bankCode);
    }
    return names.iterator().next();
  }

  @Nonnull

  public String findCode(@Nonnull String name) {
    Collection<String> codes = getCodes(name);
    if (codes.isEmpty()) {
      throw new IllegalArgumentException("No code found for " + name);
    }
    return codes.iterator().next();
  }

  @Nonnull
  public Bank findBank(@Nonnull String code) {
    return banks.get(code);
  }

  @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
  private final Cache<String, Bank> banks = new HashedCache<String, Bank>(new Cache.Factory<String, Bank>() {
    @Nonnull
    public Bank create(@Nonnull String key) {
      return new Bank(key, findDescription(key));
    }
  });

}
