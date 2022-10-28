package com.cedarsoft.commons.javafx.threed

import javafx.geometry.Point3D
import javafx.scene.shape.TriangleMesh
import java.util.stream.IntStream

/**
 * Generates [MeshView]s.
 */
object MeshGenerator {
  /**
   * Generates the triangle mesh for the provided points
   * @param divisions: The number of divisions to approach a circle (usually 24)
   * @param numberOfCoordinates The number of coordinates
   */
  @JvmStatic
  fun generateMeshFrom3DPoints(points: FloatArray, divisions: Int, numberOfCoordinates: Int): TriangleMesh {
    //if (divisions < 3) {
    //  divisions = 3;
    //}
    val textCoords = floatArrayOf(
      0.0f, 0.0f,
      1.0f, 0.0f,
      1.0f, 1.0f,
      0.0f, 1.0f
    )

    // generate faces
    val faces: MutableList<Int> = ArrayList()
    for (i in 0 until numberOfCoordinates - 1) {
      val range0 = IntStream.concat(IntStream.range(i * divisions, (i + 1) * divisions), IntStream.of(i * divisions)).toArray()
      val range1 = IntStream.concat(IntStream.range((i + 1) * divisions, (i + 2) * divisions), IntStream.of((i + 1) * divisions)).toArray()
      for (k in 0 until divisions) {
        addTriangle(faces, range0[k], range0[k + 1], range1[k], true)
        addTriangle(faces, range0[k + 1], range1[k + 1], range1[k], false)
      }
    }
    val mesh = TriangleMesh()
    mesh.points.addAll(*points)
    mesh.texCoords.addAll(*textCoords)
    mesh.faces.addAll(*faces.stream().mapToInt { i: Int? -> i!! }.toArray())
    return mesh
  }

  @JvmStatic
  fun flatten(points3D: Collection<Point3D>): FloatArray {
    // flatten 3D coordinate
    val pointsCount = points3D.size * 3
    val points = FloatArray(pointsCount)
    var pointsIndex = 0
    for (point3D in points3D) {
      points[pointsIndex] = point3D.x.toFloat()
      points[pointsIndex + 1] = point3D.y.toFloat()
      points[pointsIndex + 2] = point3D.z.toFloat()
      pointsIndex += 3
    }
    assert(pointsIndex == pointsCount) { "$pointsIndex!=$pointsCount" }
    return points
  }

  @JvmStatic
  fun generatePoints(xCoordinates: DoubleArray, yCoordinates: DoubleArray, divisions: Int): FloatArray {
    var currentDivisions = divisions
    require(xCoordinates.size == yCoordinates.size) { xCoordinates.size.toString() + " != " + yCoordinates.size }
    val numberOfCoordinates = xCoordinates.size
    if (currentDivisions < 3) {
      currentDivisions = 3
    }

    // rotate along the x-Axis and generate 3D coordinates
    val points3D: MutableList<Point3D> = ArrayList()
    val angleIncrement = 2 * Math.PI / currentDivisions
    for (i in 0 until numberOfCoordinates) {
      val x2D = xCoordinates[i]
      val y2D = yCoordinates[i]
      var angle = 0.0
      for (division in 0 until currentDivisions) {
        val y = Math.sin(angle) * y2D
        val z = Math.cos(angle) * y2D
        val point3D = Point3D(x2D, y, z)
        points3D.add(point3D)
        angle += angleIncrement
      }
    }
    return flatten(points3D)
  }

  /**
   * Adds two triangles to the given faces list!
   */
  private fun addTriangle(faces: MutableList<in Int>, v0: Int, v1: Int, v2: Int, isFirst: Boolean) {
    val textCoords: IntArray
    textCoords = if (isFirst) {
      intArrayOf(0, 1, 3)
    } else {
      intArrayOf(1, 2, 3)
    }
    // front face, vertices counter-clockwise, start vertex does not matter
    //System.out.println("front (counter-clockwise): " + isFirst + ": " + v0 + " " + v1 + " " + v2);
    faces.add(v0)
    faces.add(textCoords[0])
    faces.add(v1)
    faces.add(textCoords[1])
    faces.add(v2)
    faces.add(textCoords[2])

    // back face, vertices clockwise, start vertex does not matter
    //System.out.println("back (clockwise): " + isFirst + ": " + v0 + " " + v2 + " " + v1);
    faces.add(v0)
    faces.add(textCoords[0])
    faces.add(v2)
    faces.add(textCoords[2])
    faces.add(v1)
    faces.add(textCoords[1])
  }
}
