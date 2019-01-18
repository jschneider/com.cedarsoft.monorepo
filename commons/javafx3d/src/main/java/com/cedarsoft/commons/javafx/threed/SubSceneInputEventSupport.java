package com.cedarsoft.commons.javafx.threed;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.other.deg;

import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * A utility class that converts {@link MouseEvent} events and {@link ScrollEvent} events
 * into rotations of a {@link Node} which must be a child of a {@link SubScene}.
 */
public class SubSceneInputEventSupport {
  @deg
  private static final int ANGLE_FULL_CIRCLE = 360;

  private double mousePosX;
  private double mousePosY;
  private double mouseOldX;
  private double mouseOldY;

  @Nonnull 
  private final Rotate rotateX;
  @Nonnull 
  private final Rotate rotateY;
  @Nonnull 
  private final Rotate rotateZ;
  @Nonnull
  private final Translate cameraTranslate;

  public SubSceneInputEventSupport(@Nonnull SubScene subScene) {
    rotateX = new Rotate(0, Rotate.X_AXIS);
    rotateY = new Rotate(0, Rotate.Y_AXIS);
    rotateZ = new Rotate(0, Rotate.Z_AXIS);

    Camera camera = subScene.getCamera();
    cameraTranslate = new Translate(0, 0, 0);
    camera.getTransforms().add(cameraTranslate);

    subScene.setOnScroll(event -> {
      double scale = getTranslateZScale(event);
      double oldZ = cameraTranslate.getZ();
      double newZ = oldZ + event.getDeltaY() * scale;
      cameraTranslate.setZ(newZ);
    });

    subScene.setOnMousePressed(event -> {
      mousePosX = event.getSceneX();
      mousePosY = event.getSceneY();
      mouseOldX = mousePosX;
      mouseOldY = mousePosY;
      if (event.getClickCount() == 2) {
        resetAll();
      }
    });

    subScene.setOnMouseDragged(event -> {
      mouseOldX = mousePosX;
      mouseOldY = mousePosY;
      mousePosX = event.getSceneX();
      mousePosY = event.getSceneY();

      final double mouseDeltaX = mousePosX - mouseOldX;
      final double mouseDeltaY = mousePosY - mouseOldY;

      if (event.isPrimaryButtonDown()) {
        if (event.isAltDown()) { //roll
          if (Math.abs(mouseDeltaX) > Math.abs(mouseDeltaY)) {
            rotateZ.setAngle(computeNewAngle(rotateZ.getAngle(), mouseDeltaX * getRotateZScale(event)));
          }
          else {
            rotateZ.setAngle(computeNewAngle(rotateZ.getAngle(), mouseDeltaY * getRotateZScale(event)));
          }
        }
        else {
          double scale = getRotateXYScale(event);
          rotateY.setAngle(computeNewAngle(rotateY.getAngle(), mouseDeltaX * scale));
          rotateX.setAngle(computeNewAngle(rotateX.getAngle(), -mouseDeltaY * scale));
        }
      }
      else {
        double oldX = cameraTranslate.getX();
        double newX = oldX - mouseDeltaX * getTranslateXYScale(event);
        cameraTranslate.setX(newX);

        double oldY = cameraTranslate.getY();
        double newY = oldY - mouseDeltaY * getTranslateXYScale(event);
        cameraTranslate.setY(newY);
      }
    });

  }

  private static double computeNewAngle(double currentAngle, double offset) {
    return (currentAngle + offset) % ANGLE_FULL_CIRCLE;
  }

  public void install(@Nonnull Node node) {
    node.getTransforms().addAll(rotateX, rotateY, rotateZ);
  }

  public void resetRotation() {
    rotateX.setAngle(0);
    rotateY.setAngle(0);
    rotateZ.setAngle(0);
  }

  public void resetTranslation() {
    cameraTranslate.setX(0);
    cameraTranslate.setY(0);
    cameraTranslate.setZ(0);
  }

  public void resetAll() {
    resetTranslation();
    resetRotation();
  }

  @SuppressWarnings("MagicNumber")
  private static double getRotateXYScale(@Nonnull MouseEvent event) {
    double scale = 0.75;
    if (event.isControlDown()) {
      scale = 0.1;
    }
    if (event.isShiftDown()) {
      scale = 2.0;
    }
    return scale;
  }

  @SuppressWarnings("MagicNumber")
  private static double getRotateZScale(@Nonnull MouseEvent event) {
    double scale = 2.0;
    if (event.isControlDown()) {
      scale = 1.0;
    }
    if (event.isShiftDown()) {
      scale = 5.0;
    }
    return scale;
  }

  @SuppressWarnings("MagicNumber")
  private static double getTranslateXYScale(@Nonnull MouseEvent event) {
    double scale = 2.0;
    if (event.isControlDown()) {
      scale = 0.5;
    }
    if (event.isShiftDown()) {
      scale = 5.0;
    }
    return scale;
  }

  @SuppressWarnings("MagicNumber")
  private static double getTranslateZScale(@Nonnull GestureEvent event) {
    double scale = 25.0;
    if (event.isControlDown()) {
      scale = 2.5;
    }
    if (event.isShiftDown()) {
      scale = 75.0;
    }
    return scale;
  }

}
