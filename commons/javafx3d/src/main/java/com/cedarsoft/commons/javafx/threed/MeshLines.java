package com.cedarsoft.commons.javafx.threed;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.composites.PolyLine3D;

import javafx.collections.ObservableFloatArray;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;

/**
 * Draws a line for each edge of a {@link TriangleMesh}.
 */
public final class MeshLines extends Group {
  @Nonnull
  private final Color lineColor;
  @Nonnull
  private final PolyLine3D.LineType lineType;

  private final float lineWidth;
  private final int divisions;
  private final float offset;

  public MeshLines(@Nonnull TriangleMesh mesh, 
                   final int divisions) {
    this(mesh, Color.DARKGRAY, divisions);
  }

  public MeshLines(@Nonnull TriangleMesh mesh, 
                   @Nonnull Color lineColor, 
                   final int divisions) {
    this(mesh, lineColor, PolyLine3D.LineType.TRIANGLE, divisions);
  }

  public MeshLines(@Nonnull TriangleMesh mesh, 
                   @Nonnull Color lineColor, 
                   @Nonnull PolyLine3D.LineType lineType, 
                   final int divisions) {
    this(mesh, lineColor, lineType, 1.0f, 0.0f, divisions);
  }

  public MeshLines(@Nonnull TriangleMesh mesh,
                   @Nonnull Color lineColor,
                   @Nonnull PolyLine3D.LineType lineType,
                   final float lineWidth,
                   final float offset,
                   final int divisions) {
    this.lineType = lineType;
    this.lineWidth = lineWidth;
    this.offset = offset;
    this.divisions = divisions;
    this.lineColor = lineColor;

    getChildren().addAll(createPolyLines(mesh));

    mesh.getPoints().addListener((observableArray, sizeChanged, from, to) -> {
      getChildren().clear();
      getChildren().addAll(createPolyLines(mesh));
    });
  }

  @Nonnull
  private List<PolyLine3D> createPolyLines(@Nonnull TriangleMesh mesh) {
    ObservableFloatArray meshPoints = mesh.getPoints();
    float[] coordinates = meshPoints.toArray(new float[0]);
    int pointsCount = coordinates.length / 3;

    //The amount of slices
    int slicesCount = pointsCount / divisions;

    List<PolyLine3D> polyLines = new ArrayList<>();

    {
      //Create the "slices" for each division
      for (int sliceIndex = 0; sliceIndex < slicesCount; sliceIndex++) {
        int baseIndex = sliceIndex * 3 * divisions;

        List<Point3D> points = new ArrayList<>();
        for (int divisionIndex = 0; divisionIndex < divisions; divisionIndex++) {
          float x = coordinates[baseIndex + divisionIndex * 3];
          float y = coordinates[baseIndex + divisionIndex * 3 + 1];
          float z = coordinates[baseIndex + divisionIndex * 3 + 2];

          points.add(new Point3D(x, y, z));
        }

        //close the slice
        points.add(points.get(0));

        // apply offset
        points.forEach(this::applyOffset);

        polyLines.add(new PolyLine3D(points, lineWidth, getLineColor(), lineType));
      }
    }

    //Create the lines along the x coordinates
    {
      for (int divisionIndex = 0; divisionIndex < divisions; divisionIndex++) {

        List<Point3D> points = new ArrayList<>();
        for (int sliceIndex = 0; sliceIndex < slicesCount; sliceIndex++) {
          int baseIndex = (divisionIndex + sliceIndex * divisions) * 3;

          float x = coordinates[baseIndex];
          float y = coordinates[baseIndex + 1];
          float z = coordinates[baseIndex + 2];

          points.add(new Point3D(x, y, z));
        }

        // apply offset
        points.forEach(this::applyOffset);

        polyLines.add(new PolyLine3D(points, lineWidth, getLineColor(), lineType));
      }
    }

    return polyLines;
  }

  private void applyOffset(@Nonnull Point3D point3D) {
    point3D.y = applyOffset(point3D.y);
    point3D.z = applyOffset(point3D.z);
  }

  private float applyOffset(float value) {
    // TODO
    // This does not work for folds. A positive value alone
    // is no indicator whether a positive offset or a negative
    // offset must be used.
    if (offset == 0.0) {
      return value;
    }
    if (value < 0) {
      return value - offset;
    }
    return value + offset;
  }

  @Nonnull
  public Color getLineColor() {
    return lineColor;
  }

}
