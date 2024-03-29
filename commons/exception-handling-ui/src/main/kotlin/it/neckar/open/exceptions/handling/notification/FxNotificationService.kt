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
package it.neckar.open.exceptions.handling.notification

import it.neckar.open.annotations.UiThread
import it.neckar.open.javafx.FxUtils
import it.neckar.open.unit.si.ms
import javafx.beans.InvalidationListener
import javafx.scene.control.ButtonType
import javafx.stage.Stage
import net.java.balloontip.styles.ToolTipBalloonStyle
import java.awt.Color
import java.util.LinkedList
import javax.annotation.concurrent.ThreadSafe
import javax.swing.Timer

/**
 * Shows notifications in a popup
 */
@ThreadSafe
class FxNotificationService {
  /**
   * The notification queue that contains the notifications that should be shown
   */
  @UiThread
  private val notificationQueue = LinkedList<Notification>()
  /**
   * Whether there is currently a notification visible
   */
  @UiThread
  private var notificationVisible = false

  /**
   * Show the given notification as soon as possible.
   * Will add the notification to the queue if another notification is currently visible
   */
  @UiThread
  fun showNotification(notification: Notification) {
    if (notificationVisible) {
      notificationQueue.add(notification)
      return
    }

    val dialog = object : FxBalloonDialog(notification) {
      override fun closeBalloon() {
        result = ButtonType.OK
        close()

        notificationVisible = false

        //Look if there is a notification available
        val nextNotification = notificationQueue.poll() ?: return

        //Wait for 1200 ms until the notification is shown
        //TODO Convert to JavaFX
        val timer = Timer(1200) { _ -> showNotification(nextNotification) }
        timer.isRepeats = false
        timer.start()
      }
    }

    notificationVisible = true


    val stage = FxUtils.stage
    dialog.initOwner(stage)
    position(dialog, stage)
    dialog.show()

    position(dialog, stage)
    val updatePositionListener = InvalidationListener {
      position(dialog, stage)
    }
    dialog.widthProperty().addListener(updatePositionListener)
    dialog.heightProperty().addListener(updatePositionListener)
    stage.xProperty().addListener(updatePositionListener)
    stage.yProperty().addListener(updatePositionListener)
    stage.widthProperty().addListener(updatePositionListener)
    stage.heightProperty().addListener(updatePositionListener)

    //TimingUtils.showTimedBalloon(balloonTip, VISIBILITY_TIME);
    //TODO add timing to hide it
  }

  private fun position(dialog: FxBalloonDialog, stage: Stage) {
    dialog.x = stage.x + stage.width - dialog.width - 15
    dialog.y = stage.y + stage.height - dialog.height - 15
  }

  companion object {
    const val VISIBILITY_TIME: @ms Int = 15000

    val STYLE: ToolTipBalloonStyle = ToolTipBalloonStyle(Color(255, 251, 192), Color.GRAY)
  }
}
