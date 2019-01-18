package com.cedarsoft.test.javafx;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class MyComp extends Group {
  public Translate t = new Translate();
  public Translate p = new Translate();
  public Translate ip = new Translate();
  public Rotate rx = new Rotate();

  {
    rx.setAxis(Rotate.X_AXIS);
  }

  public Rotate ry = new Rotate();

  {
    ry.setAxis(Rotate.Y_AXIS);
  }

  public Rotate rz = new Rotate();

  {
    rz.setAxis(Rotate.Z_AXIS);
  }

  public Scale s = new Scale();

  public MyComp() {
    getTransforms().addAll(t, p, rz, ry, rx, s, ip);


    final PhongMaterial redMaterial = new PhongMaterial();
    redMaterial.setSpecularColor(Color.ORANGE);
    redMaterial.setDiffuseColor(Color.RED);

    final PhongMaterial blueMaterial = new PhongMaterial();
    blueMaterial.setDiffuseColor(Color.BLUE);
    blueMaterial.setSpecularColor(Color.LIGHTBLUE);

    final PhongMaterial greyMaterial = new PhongMaterial();
    greyMaterial.setDiffuseColor(Color.DARKGREY);
    greyMaterial.setSpecularColor(Color.GREY);

    final Box red = new Box(400, 400, 400);
    red.setMaterial(redMaterial);

    final Sphere blue = new Sphere(200);
    blue.setMaterial(blueMaterial);

    final Cylinder grey = new Cylinder(5, 100);
    grey.setMaterial(greyMaterial);


    getChildren().add(red);
    getChildren().add(blue);
    getChildren().add(grey);
  }
}
