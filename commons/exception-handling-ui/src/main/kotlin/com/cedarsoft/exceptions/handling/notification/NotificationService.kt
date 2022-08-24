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
package com.cedarsoft.exceptions.handling.notification

import com.cedarsoft.annotations.UiThread
import com.cedarsoft.exceptions.handling.Messages
import com.cedarsoft.swing.common.Fonts
import com.cedarsoft.swing.common.SwingHelper
import com.cedarsoft.swing.common.dialog.AbstractDialog
import com.cedarsoft.unit.si.ms
import net.java.balloontip.BalloonTip
import net.java.balloontip.CustomBalloonTip
import net.java.balloontip.positioners.BalloonTipPositioner
import net.java.balloontip.styles.ToolTipBalloonStyle
import net.java.balloontip.utils.TimingUtils
import net.miginfocom.swing.MigLayout
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.ActionEvent
import java.util.ArrayDeque
import javax.annotation.concurrent.ThreadSafe
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.Timer

/**
 * Shows notifications in a popup
 */
@ThreadSafe
class NotificationService {
  @UiThread
  private val notificationQueue = ArrayDeque<Notification>()

  private var notificationVisible: Boolean = false

  @UiThread
  fun showNotification(notification: Notification) {
    if (notificationVisible) {
      notificationQueue.add(notification)
      return
    }

    val frame = SwingHelper.getFrameSafe() ?: throw IllegalArgumentException("No frame found")
    val mainFrameComponent = frame.contentPane as JComponent

    val balloonTip = object : CustomBalloonTip(mainFrameComponent, JPanel(), Rectangle(10, 10), Style, BottomRightPositioner(mainFrameComponent), BalloonTip.getDefaultCloseButton()) {
      override fun closeBalloon() {
        super.closeBalloon()
        notificationVisible = false

        //Look if there is a notification available
        val nextNotification = notificationQueue.poll() ?: return

        //Wait for 1200 ms until the notification is shown
        val timer = Timer(1200) { showNotification(nextNotification) }
        timer.isRepeats = false
        timer.start()
      }
    }

    val content = JPanel(MigLayout("fill, wrap 2", "[][grow,fill]", "[fill][fill,grow][fill]"))
    content.isOpaque = false

    val headlineLabel = JLabel(notification.title)
    headlineLabel.font = Fonts.TITLE

    content.add(JLabel(AbstractDialog.Icons.WARNING))
    content.add(headlineLabel, "wrap")
    content.add(JLabel(notification.message), "span")

    val detailsCallback = notification.detailsCallback
    if (detailsCallback != null) {
      content.add(SwingHelper.createHyperLink(object : AbstractAction(Messages["details"]) {
        override fun actionPerformed(e: ActionEvent) {
          balloonTip.closeBalloon()
          detailsCallback.detailsClicked(notification)
        }
      }), "alignx right, span")
    }

    balloonTip.contents = content


    notificationVisible = true
    TimingUtils.showTimedBalloon(balloonTip, VisibilityTime)
  }

  /**
   * Positions the balloon at the bottom right
   */
  private class BottomRightPositioner(private val mainFrameComponent: JComponent) : BalloonTipPositioner() {

    override fun getTipLocation(): Point {
      throw UnsupportedOperationException("not available")
    }

    override fun determineAndSetLocation(attached: Rectangle) {
      val preferredSize = balloonTip.preferredSize

      val x = mainFrameComponent.width.toDouble() - preferredSize.getWidth() - 10.0
      val y = mainFrameComponent.height.toDouble() - preferredSize.getHeight() - 15.0

      balloonTip.size = preferredSize
      balloonTip.setLocation(x.toInt(), y.toInt())
      balloonTip.revalidate()
    }

    override fun onStyleChange() {}
  }

  companion object {
    @ms
    val VisibilityTime: Int = 15000
    private val Style = ToolTipBalloonStyle(Color(255, 251, 192), Color.GRAY)
  }

}
