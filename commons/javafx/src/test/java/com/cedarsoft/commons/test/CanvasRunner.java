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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.tbee.javafx.scene.layout.MigPane;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CanvasRunner extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void init() throws Exception {
    super.init();
    Parameters parameters = getParameters();

    System.out.println("-----------");
    for (Map.Entry<String, String> entry : parameters.getNamed().entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }
    System.out.println("-----------");
    for (String s : parameters.getRaw()) {
      System.out.println("s = " + s);
    }
    System.out.println("-----------");
    for (String s : parameters.getUnnamed()) {
      System.out.println("s = " + s);
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
    System.out.println("Platform.isFxApplicationThread() = " + Platform.isFxApplicationThread());


    Text headLine = new Text("Canvas Test");
    headLine.setId("headLine");

    primaryStage.titleProperty().bind(headLine.textProperty());

    Group root = new Group();
    Canvas canvas = new Canvas(1280, 720);

    //drawShapes(canvas);

    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("CanvasRunner.handle");
        canvas.getGraphicsContext2D().clearRect(event.getX() - 2, event.getY() - 2, 5, 5);
      }
    });

    canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        canvas.getGraphicsContext2D().setFill(Color.ORANGE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        g2d.setColor(java.awt.Color.DARK_GRAY);
        g2d.drawString("Hello World", 10, 10);
      }
    });


    //Layout

    MigPane pane = new MigPane("wrap 1, align 50% 50%, fill", "[fill,grow, align center]", "[][fill,grow]");
    pane.setPadding(new Insets(25, 25, 25, 25));

    pane.add(headLine, "span");

    root.getChildren().add(canvas);
    pane.add(root);

    Scene scene = new Scene(pane, 1920, 1080);
    primaryStage.setScene(scene);
    //scene.getStylesheets().add(getClass().getResource("JavaFxRunner.css").toExternalForm());
    primaryStage.show();
  }

  private void drawShapes(@Nonnull Canvas canvas) {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setFill(Color.ORANGE);

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

    gc.setFill(Color.CYAN);
    gc.setStroke(Color.ORANGE);
    gc.beginPath();
    gc.moveTo(50, 50);
    gc.bezierCurveTo(150, 20, 150, 150, 75, 150);
    gc.closePath();
    gc.setLineWidth(10);
    gc.stroke();

    gc.applyEffect(new DropShadow(15, 6, 4, Color.DARKGREY));


    gc.setEffect(new Glow(1.0));
    gc.setFill(Color.BLACK);
    gc.setFont(Font.font(30));
    gc.setFontSmoothingType(FontSmoothingType.LCD);
    gc.fillText("Hello World", 450, 405);

    gc.setFill(Color.ORANGE);
    gc.setEffect(new Lighting(new Light.Spot()));
    gc.fillText("Hello World", 450, 435);

  }
}
