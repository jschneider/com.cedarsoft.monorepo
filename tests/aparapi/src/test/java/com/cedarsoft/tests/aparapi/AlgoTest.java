package com.cedarsoft.tests.aparapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AlgoTest {
  @Disabled
  @Test
  void testAsdf() {
    int size = 1512;


    float[] xValues = new float[size];
    float[] yValues = new float[size];
    float[] distances = new float[size];

    for (int i = 0; i < size; i++) {
      xValues[i] = i;
      yValues[i] = i;
    }


    int gid = size - 1;

    float currentXValue = xValues[gid];
    float currentYValue = yValues[gid];

    float distanceSum = 0;

    for (int i = 0; i < xValues.length; i++) {
      if (i == gid) {
        continue;
      }

      float xValue = xValues[i];
      float yValue = yValues[i];


      float deltaX = currentXValue - xValue;
      float deltaY = currentYValue - yValue;

      float thisDistance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
      distanceSum += thisDistance;
    }

    distances[gid] = distanceSum;


    Assertions.assertThat(distances[gid]).isEqualTo(123f);
  }
}
