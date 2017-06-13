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
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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
