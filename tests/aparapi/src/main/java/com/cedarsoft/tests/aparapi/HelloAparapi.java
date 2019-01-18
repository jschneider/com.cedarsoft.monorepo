package com.cedarsoft.tests.aparapi;

import com.aparapi.device.Device;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.internal.kernel.KernelManager;
import com.aparapi.internal.kernel.KernelPreferences;
import com.aparapi.internal.opencl.OpenCLPlatform;

import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class HelloAparapi {
  public static void main(String[] args) {
    List<OpenCLPlatform> platforms = new OpenCLPlatform().getOpenCLPlatforms();
    System.out.println("Machine contains " + platforms.size() + " OpenCL platforms");
    int platformc = 0;
    for (OpenCLPlatform platform : platforms) {
      System.out.println("Platform " + platformc + "{");
      System.out.println("   Name    : \"" + platform.getName() + "\"");
      System.out.println("   Vendor  : \"" + platform.getVendor() + "\"");
      System.out.println("   Version : \"" + platform.getVersion() + "\"");
      List<OpenCLDevice> devices = platform.getOpenCLDevices();
      System.out.println("   Platform contains " + devices.size() + " OpenCL devices");
      int devicec = 0;
      for (OpenCLDevice device : devices) {
        System.out.println("   Device " + devicec + "{");
        System.out.println("       Type                  : " + device.getType());
        System.out.println("       GlobalMemSize         : " + device.getGlobalMemSize());
        System.out.println("       LocalMemSize          : " + device.getLocalMemSize());
        System.out.println("       MaxComputeUnits       : " + device.getMaxComputeUnits());
        System.out.println("       MaxWorkGroupSizes     : " + device.getMaxWorkGroupSize());
        System.out.println("       MaxWorkItemDimensions : " + device.getMaxWorkItemDimensions());
        System.out.println("   }");
        devicec++;
      }
      System.out.println("}");
      platformc++;
    }

    KernelPreferences preferences = KernelManager.instance().getDefaultPreferences();
    System.out.println("\nDevices in preferred order:\n");

    for (Device device : preferences.getPreferredDevices(null)) {
      System.out.println(device);
      System.out.println();
    }
  }

}
