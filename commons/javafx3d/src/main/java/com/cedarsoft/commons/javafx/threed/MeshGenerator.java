package com.cedarsoft.commons.javafx.threed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * Generates {@link MeshView}s.
 */
public class MeshGenerator {

  private MeshGenerator() {
    // utility class
  }

  @Nonnull
  public static TriangleMesh generateMeshFrom3DPoints(float[] points, int divisions, int numberOfCoordinates) {
    //if (divisions < 3) {
    //  divisions = 3;
    //}

    float[] textCoords = {0.0f, 0.0f,
                          1.0f, 0.0f,
                          1.0f, 1.0f,
                          0.0f, 1.0f
    };

    // generate faces
    List<Integer> faces = new ArrayList<>();
    for (int i = 0; i < numberOfCoordinates - 1; i++) {
      int[] range0 = IntStream.concat(IntStream.range(i * divisions, (i + 1) * divisions), IntStream.of(i * divisions)).toArray();
      int[] range1 = IntStream.concat(IntStream.range((i + 1) * divisions, (i + 2) * divisions), IntStream.of((i + 1) * divisions)).toArray();

      for (int k = 0; k < divisions; k++) {
        addTriangle(faces, range0[k], range0[k + 1], range1[k], true);
        addTriangle(faces, range0[k + 1], range1[k + 1], range1[k], false);
      }
    }

    TriangleMesh mesh = new TriangleMesh();
    mesh.getPoints().addAll(points);
    mesh.getTexCoords().addAll(textCoords);
    mesh.getFaces().addAll(faces.stream().mapToInt(i -> i).toArray());

    return mesh;
  }

  @Nonnull 
  public static float[] flatten(@Nonnull Collection<? extends Point3D> points3D) {
    // flatten 3D coordinate
    final int pointsCount = points3D.size() * 3;
    float[] points = new float[pointsCount];
    int pointsIndex = 0;
    for (Point3D point3D : points3D) {
      points[pointsIndex] = (float) point3D.getX();
      points[pointsIndex + 1] = (float) point3D.getY();
      points[pointsIndex + 2] = (float) point3D.getZ();
      pointsIndex += 3;
    }
    assert pointsIndex == pointsCount : pointsIndex + "!=" + pointsCount;
    return points;
  }
  
  @Nonnull 
  public static float[] generatePoints(@Nonnull double[] xCoordinates, @Nonnull double[] yCoordinates, int divisions) {
    if (xCoordinates.length != yCoordinates.length) {
      throw new IllegalArgumentException(xCoordinates.length + " != " + yCoordinates.length);
    }
    int numberOfCoordinates = xCoordinates.length;

    if (divisions < 3) {
      divisions = 3;
    }

    // rotate along the x-Axis and generate 3D coordinates
    List<Point3D> points3D = new ArrayList<>();
    final double angleIncrement = 2 * Math.PI / divisions;
    for (int i = 0; i < numberOfCoordinates; i++) {
      double x2D = xCoordinates[i];
      double y2D = yCoordinates[i];
      double angle = 0.0;
      for (int division = 0; division < divisions; division++) {
        double x = x2D;
        double y = Math.sin(angle) * y2D;
        double z = Math.cos(angle) * y2D;
        Point3D point3D = new Point3D(x, y, z);
        points3D.add(point3D);
        angle += angleIncrement;
      }
    }

    return flatten(points3D);
  }

  private static void addTriangle(@Nonnull List<? super Integer> faces, int v0, int v1, int v2, boolean isFirst) {
    int[] textCoords;
    if (isFirst) {
      textCoords = new int[]{0, 1, 3};
    }
    else {
      textCoords = new int[]{1, 2, 3};
    }
    // front face, vertices counter-clockwise, start vertex does not matter
    //System.out.println("front (counter-clockwise): " + isFirst + ": " + v0 + " " + v1 + " " + v2);
    faces.add(v0);
    faces.add(textCoords[0]);
    faces.add(v1);
    faces.add(textCoords[1]);
    faces.add(v2);
    faces.add(textCoords[2]);

    // back face, vertices clockwise, start vertex does not matter
    //System.out.println("back (clockwise): " + isFirst + ": " + v0 + " " + v2 + " " + v1);
    faces.add(v0);
    faces.add(textCoords[0]);
    faces.add(v2);
    faces.add(textCoords[2]);
    faces.add(v1);
    faces.add(textCoords[1]);
  }
}
