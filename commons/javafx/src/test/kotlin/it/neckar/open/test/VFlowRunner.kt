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
package it.neckar.open.test

import eu.mihosoft.vrl.workflow.FlowFactory
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage

/**
 */
class VFlowRunner : Application() {

  override fun start(primaryStage: Stage) {
    val flow = FlowFactory.newFlow()
    flow.isVisible = true
    val n1 = flow.newNode()
    n1.id = "id1"
    val n2 = flow.newNode()
    n1.addInput("data")
    n1.addOutput("data")
    n2.addInput("data")
    n2.addOutput("data")
    val connectionResult = flow.connect(n1, n2, "data")
    println("connectionResult = $connectionResult")
    n1.x = 10.0
    n1.y = 10.0
    n1.width = 160.0
    n1.height = 100.0
    n2.x = 110.0
    n2.y = 10.0
    n2.width = 160.0
    n2.height = 100.0

    //ScalableContentPane canvas = new ScalableContentPane();
    val canvas = Pane()
    canvas.style = "-fx-background-color: linear-gradient(to bottom, rgb(10,32,60), rgb(42,52,120));"
    val fXSkinFactory = FXSkinFactory(canvas)
    flow.setSkinFactories(fXSkinFactory)

    primaryStage.scene = Scene(canvas, 1920.0, 1080.0)
    primaryStage.show()
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      launch(*args)
    }
  }
}
