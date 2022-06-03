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
package com.cedarsoft.commons.test;

import eu.mihosoft.vrl.workflow.ConnectionResult;
import eu.mihosoft.vrl.workflow.FlowFactory;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import eu.mihosoft.vrl.workflow.fx.ScalableContentPane;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.tbee.javafx.scene.layout.MigPane;

/**
 */
public class VFlowRunner extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    VFlow flow = FlowFactory.newFlow();
    flow.setVisible(true);

    VNode n1 = flow.newNode();
    n1.setId("id1");
    VNode n2 = flow.newNode();

    n1.addInput("data");
    n1.addOutput("data");
    n2.addInput("data");
    n2.addOutput("data");

    ConnectionResult connectionResult = flow.connect(n1, n2, "data");
    System.out.println("connectionResult = " + connectionResult);


    n1.setX(10);
    n1.setY(10);
    n1.setWidth(160);
    n1.setHeight(100);

    n2.setX(110);
    n2.setY(10);
    n2.setWidth(160);
    n2.setHeight(100);

    //ScalableContentPane canvas = new ScalableContentPane();
    Pane canvas = new Pane();
    canvas.setStyle("-fx-background-color: linear-gradient(to bottom, rgb(10,32,60), rgb(42,52,120));");

    FXSkinFactory fXSkinFactory = new FXSkinFactory(canvas);
    flow.setSkinFactories(fXSkinFactory);

    primaryStage.setScene(new Scene(canvas, 1920, 1080));
    primaryStage.show();
  }
}
