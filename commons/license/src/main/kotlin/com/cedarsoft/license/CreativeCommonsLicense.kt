/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package com.cedarsoft.license

import java.net.MalformedURLException
import java.net.URL
import java.util.Locale

/**
 *
 * CreativeCommonsLicense class.
 *
 */
class CreativeCommonsLicense(
  id: String,
  name: String,
  val isRestrictedToNonCommercial: Boolean,
  val modificationsAllowed: ModificationsAllowed,
  url: String,
) : License(id, name, url) {

  val isDerivedWorkAllowed: Boolean
    get() = modificationsAllowed == ModificationsAllowed.YES

  val isUsableCommercially: Boolean
    get() = !isRestrictedToNonCommercial

  val isSharedAlikeDerivedWorkAllowed: Boolean
    get() = modificationsAllowed == ModificationsAllowed.SHARE_ALIKE || modificationsAllowed == ModificationsAllowed.YES

  /**
   * Returns the translated URL
   *
   * @param locale the locale
   * @return the URL for the locale
   */
  fun getUrl(locale: Locale): URL {
    val urlBase = url!!
    return try {
      URL(urlBase.protocol, urlBase.host, urlBase.port, urlBase.file + "/" + locale.language)
    } catch (e: MalformedURLException) {
      throw RuntimeException(e)
    }
  }

  enum class ModificationsAllowed {
    YES, SHARE_ALIKE, NO
  }

  companion object {
    operator fun get(id: String): License {
      for (license in CC_LICENSES) {
        if (license.id == id) {
          return license
        }
      }
      throw IllegalArgumentException("No license found for id <$id>")
    }
  }
}
