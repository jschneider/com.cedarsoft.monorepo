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
package com.cedarsoft.exceptions.handling

import com.cedarsoft.exceptions.ApplicationException
import com.cedarsoft.i18n.DefaultI18nConfiguration
import com.cedarsoft.swing.common.dialog.AbstractDialog
import com.cedarsoft.swing.common.dialog.ComponentSizeStorage
import com.jidesoft.dialog.BannerPanel
import com.jidesoft.swing.StyledLabel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingConstants

/**
 * Shows an application exception
 */
class ApplicationExceptionDialog(
  parent: JFrame?,
  private val e: ApplicationException,
  preferences: ComponentSizeStorage.Backend?
) : AbstractDialog(parent, preferences) {

  override fun createBannerPanel(): JComponent {
    val bannerPanel = BannerPanel(e.errorCode.toString(), e.getTitle(DefaultI18nConfiguration))
    title = e.getTitle(DefaultI18nConfiguration) + " (" + e.errorCode + ")"
    return bannerPanel
  }

  override fun createContentPanel(): JComponent {
    val panel = object : JPanel(BorderLayout()) {
      override fun getPreferredSize(): Dimension {
        val preferredSize = super.getPreferredSize()
        preferredSize.width = Math.max(preferredSize.width, 500)
        return preferredSize
      }
    }

    val styledLabel = StyledLabel(e.getLocalizedMessage(DefaultI18nConfiguration))
    panel.add(styledLabel)

    styledLabel.verticalAlignment = SwingConstants.TOP
    styledLabel.rows = 5
    styledLabel.maxRows = 7
    styledLabel.isLineWrap = true

    return panel
  }
}
