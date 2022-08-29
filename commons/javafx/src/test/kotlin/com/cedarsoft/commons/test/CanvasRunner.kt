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
package com.cedarsoft.commons.test

import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Glow
import javafx.scene.effect.Light
import javafx.scene.effect.Lighting
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontSmoothingType
import javafx.scene.text.Text
import javafx.stage.Stage
import org.jfree.fx.FXGraphics2D
import org.tbee.javafx.scene.layout.MigPane
import javax.annotation.Nonnull

/**
 */
class CanvasRunner : Application() {
  @Throws(Exception::class)
  override fun init() {
    super.init()
    val parameters = parameters
    println("-----------")
    for ((key, value) in parameters.named) {
      println("$key: $value")
    }
    println("-----------")
    for (s in parameters.raw) {
      println("s = $s")
    }
    println("-----------")
    for (s in parameters.unnamed) {
      println("s = $s")
    }
  }

  @Throws(Exception::class)
  override fun start(primaryStage: Stage) {
    println("Thread.currentThread().getName() = " + Thread.currentThread().name)
    println("Platform.isFxApplicationThread() = " + Platform.isFxApplicationThread())
    val headLine = Text("Canvas Test")
    headLine.id = "headLine"
    primaryStage.titleProperty().bind(headLine.textProperty())
    val root = Group()
    val canvas = Canvas(1280.0, 720.0)

    //drawShapes(canvas);
    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED) { event ->
      println("CanvasRunner.handle")
      canvas.graphicsContext2D.clearRect(event.x - 2, event.y - 2, 5.0, 5.0)
    }
    canvas.addEventHandler(MouseEvent.MOUSE_CLICKED) {
      canvas.graphicsContext2D.fill = Color.ORANGE
      canvas.graphicsContext2D.fillRect(0.0, 0.0, canvas.width, canvas.height)
      val g2d = FXGraphics2D(canvas.graphicsContext2D)
      g2d.color = java.awt.Color.DARK_GRAY
      g2d.drawString("Hello World", 10, 10)
    }


    //Layout
    val pane = MigPane("wrap 1, align 50% 50%, fill", "[fill,grow, align center]", "[][fill,grow]")
    pane.padding = Insets(25.0, 25.0, 25.0, 25.0)
    pane.add(headLine, "span")
    root.children.add(canvas)
    pane.add(root)
    val scene = Scene(pane, 1920.0, 1080.0)
    primaryStage.scene = scene
    //scene.getStylesheets().add(getClass().getResource("JavaFxRunner.css").toExternalForm());
    primaryStage.show()
  }

  private fun drawShapes(@Nonnull canvas: Canvas) {
    val gc = canvas.graphicsContext2D
    gc.fill = Color.ORANGE

    //gc.setLineWidth(5);
    //gc.strokeLine(40, 10, 10, 40);
    //gc.fillOval(10, 60, 30, 30);
    //
    //gc.setFill(Color.AZURE);
    //gc.fillText("Hello World", 10, 10);
    //
    //{
    //  FXGraphics2D g2d = new FXGraphics2D(gc);
    //  g2d.setColor(java.awt.Color.BLUE);
    //  g2d.fill(new Rectangle2D.Double(200, 210, 110, 120));
    //}
    gc.fill = Color.CYAN
    gc.stroke = Color.ORANGE
    gc.beginPath()
    gc.moveTo(50.0, 50.0)
    gc.bezierCurveTo(150.0, 20.0, 150.0, 150.0, 75.0, 150.0)
    gc.closePath()
    gc.lineWidth = 10.0
    gc.stroke()
    gc.applyEffect(DropShadow(15.0, 6.0, 4.0, Color.DARKGREY))
    gc.setEffect(Glow(1.0))
    gc.fill = Color.BLACK
    gc.font = Font.font(30.0)
    gc.fontSmoothingType = FontSmoothingType.LCD
    gc.fillText("Hello World", 450.0, 405.0)
    gc.fill = Color.ORANGE
    gc.setEffect(Lighting(Light.Spot()))
    gc.fillText("Hello World", 450.0, 435.0)
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      launch(*args)
    }
  }
}
