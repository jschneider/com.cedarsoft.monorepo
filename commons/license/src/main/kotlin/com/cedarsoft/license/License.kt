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

/**
 * Represents the license of the image
 *
 * @noinspection ClassReferencesSubclass
 */
open class License
@JvmOverloads constructor(
  val id: String,
  val name: String,
  val url: URL? = null,
) {

  constructor(id: String, name: String, url: String) : this(id, name, getUrl(url)) {}

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is License) return false
    return id == other.id
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun toString(): String {
    return "$name ($id)"
  }

  companion object {
    /**
     * Constant `UNKNOWN`
     */
    @JvmField
    val UNKNOWN: License = License("UNKNOWN", "Unknown")

    /**
     * Constant `ALL_RIGHTS_RESERVED`
     */
    @JvmField
    val ALL_RIGHTS_RESERVED: License = License("ALL_RIGHTS_RESERVED", "All rights reserved")

    /**
     * Constant `PUBLIC_DOMAIN`
     */
    @JvmField
    val PUBLIC_DOMAIN: License = License("PUBLIC_DOMAIN", "Public Domain")

    @JvmField
    val CDDL: License = License("CDDL", "Common Development and Distribution License", "http://www.opensource.org/licenses/cddl1.php")

    @JvmField
    val AFFERO_GPL: License = License("AFFERO GPL", "Affero GNU Public License", "http://www.opensource.org/licenses/agpl-v3.html")

    @JvmField
    val APACHE_20: License = License("APACHE 2.0", "Apache License 2.0", "http://www.opensource.org/licenses/apache2.0.php")

    @JvmField
    val BSD_NEW: License = License("BSD NEW", "New and Simplified BSD licenses", "http://www.opensource.org/licenses/bsd-license.php")

    @JvmField
    val CPAL: License = License("CPAL", "Common Public Attribution License 1.0", "http://www.opensource.org/licenses/cpal_1.0")

    @JvmField
    val EPL: License = License("EPL", "Eclipse Public License", "http://www.opensource.org/licenses/eclipse-1.0.php")

    @JvmField
    val GPL_2: License = License("GPLv2", "GNU General Public License 2.0", "http://www.opensource.org/licenses/gpl-2.0.php")

    @JvmField
    val GPL_3: License = License("GPLv3", "GNU General Public License 3.0", "http://www.opensource.org/licenses/gpl-3.0.html")

    @JvmField
    val LGPL: License = License("LGPL", "GNU Library or \"Lesser\" General Public License", "http://www.opensource.org/licenses/lgpl-2.1.php")

    @JvmField
    val LGPL_3: License = License("LGPLv3", "GNU Library or \"Lesser\" General Public License version 3.0 (LGPLv3)", "http://www.opensource.org/licenses/lgpl-3.0.html")

    @JvmField
    val MPL: License = License("MPL", "Mozilla Public License 1.1 (MPL)", "http://www.opensource.org/licenses/mozilla1.1.php")

    /**
     * Creative Commons
     */
    @JvmField
    val CC_BY: CreativeCommonsLicense = CreativeCommonsLicense("CC-BY", "CC Attribution", false, CreativeCommonsLicense.ModificationsAllowed.YES, "http://creativecommons.org/licenses/by/3.0")

    /**
     * Share-Alike
     */
    @JvmField
    val CC_BY_SA: CreativeCommonsLicense = CreativeCommonsLicense("CC-BY-SA", "CC Attribution Share Alike", false, CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, "http://creativecommons.org/licenses/by-sa/3.0")

    /**
     * No-Derivative-Work
     */
    @JvmField
    val CC_BY_ND: CreativeCommonsLicense = CreativeCommonsLicense("CC-BY-ND", "CC Attribution No Derivates", false, CreativeCommonsLicense.ModificationsAllowed.NO, "http://creativecommons.org/licenses/by-nd/3.0")

    /**
     * Non-Commercial
     */
    @JvmField
    val CC_BY_NC: CreativeCommonsLicense = CreativeCommonsLicense("CC-BY-NC", "CC Attribution Non-Commercial", true, CreativeCommonsLicense.ModificationsAllowed.YES, "http://creativecommons.org/licenses/by-nc/3.0")

    /**
     * Non-Commercial, Share-Alike
     */
    @JvmField
    val CC_BY_NC_SA: CreativeCommonsLicense = CreativeCommonsLicense("CC-BY-NC-SA", "CC Attribution Non-Commercial Share Alike", true, CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, "http://creativecommons.org/licenses/by-nc-sa/3.0")

    /**
     * Non-Commercial, No-Derivative-Work
     */
    @JvmField
    val CC_BY_NC_ND: CreativeCommonsLicense = CreativeCommonsLicense("CC-BY-NC-ND", "CC Attribution Non-Commercial No Derivates", true, CreativeCommonsLicense.ModificationsAllowed.NO, "http://creativecommons.org/licenses/by-nc-nd/3.0")

    @JvmField
    val CC_LICENSES: List<CreativeCommonsLicense> = listOf(
      CC_BY, CC_BY_SA, CC_BY_ND, CC_BY_NC, CC_BY_NC_SA, CC_BY_NC_ND
    )

    @JvmField
    val LICENSES: List<License> = buildList {
      addAll(
        listOf(
          UNKNOWN, ALL_RIGHTS_RESERVED, PUBLIC_DOMAIN, CDDL, AFFERO_GPL, APACHE_20, BSD_NEW, CPAL, EPL, GPL_2, GPL_3, LGPL, LGPL_3, MPL
        )
      )

      addAll(CC_LICENSES)
    }

    @JvmStatic
    fun getUrl(url: String): URL {
      return try {
        URL(url)
      } catch (e: MalformedURLException) {
        throw RuntimeException(e)
      }
    }

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    operator fun get(id: String): License {
      for (license in LICENSES) {
        if (license.id == id) {
          return license
        }
      }
      throw IllegalArgumentException("No license found for id <$id>")
    }
  }
}
