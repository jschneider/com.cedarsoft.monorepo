package com.cedarsoft.tests.aparapi;

import com.aparapi.Kernel;
import com.aparapi.Range;

public class AparapiDistance {

  public static void main(String[] args) {

    System.setProperty("com.aparapi.dumpFlags", "true");
    System.setProperty("com.aparapi.enableShowGeneratedOpenCL", "true");
    System.setProperty("com.aparapi.enableProfiling", "true");
    System.setProperty("com.aparapi.enableExecutionModeReporting", "true");
    System.setProperty("com.aparapi.enableVerboseJNIOpenCLResourceTracking", "true");
    System.setProperty("com.aparapi.executionMode", "GPU");
    System.setProperty("com.aparapi.executionMode", "CPU");

    System.setProperty("com.aparapi.dumpProfilesOnExit", "true");


    final int size = 700;

    final float[] xValues = new float[size];
    final float[] yValues = new float[size];

    /** Initialize input array. */
    for (int i = 0; i < size; i++) {
      xValues[i] = i;
      yValues[i] = i;
    }

    /** Output array*/
    final float[] distances = new float[size];

    /** Aparapi Kernel which computes squares of input array elements and populates them in corresponding elements of
     * output array.
     **/
    Kernel kernel = new Kernel() {
      @Override
      public void run() {
        int gid = getGlobalId();

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

          float thisDistance = sqrt(deltaX * deltaX + deltaY * deltaY);
          distanceSum += thisDistance;
        }

        distances[gid] = distanceSum;
      }
    };

    // Execute Kernel.

    kernel.execute(Range.create(512));

    // Report target execution mode: GPU or JTP (Java Thread Pool).
    System.out.println("Device = " + kernel.getTargetDevice().getShortDescription());

    // Display computed square values.
    if (true) {
      for (int i = Math.max(size - 500, 0); i < size; i++) {
        System.out.printf("%6.0f %8.0f\n", xValues[i], distances[i]);
      }
    }

    // Dispose Kernel resources.
    kernel.dispose();


    //Run the same on the CPU

    long start = System.currentTimeMillis();

    for (int gid = 0; gid < size; gid++) {
      float currentXValue = xValues[gid];
      float currentYValue = yValues[gid];

      double distanceSum = 0;

      for (int i = 0; i < xValues.length; i++) {
        if (i == gid) {
          continue;
        }

        float xValue = xValues[i];
        float yValue = yValues[i];


        float deltaX = currentXValue - xValue;
        float deltaY = currentYValue - yValue;

        double thisDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        distanceSum += thisDistance;
      }

      distances[gid] = (float) distanceSum;
    }

    System.out.println("CPU took " + (System.currentTimeMillis() - start) + " ms");
  }

}
