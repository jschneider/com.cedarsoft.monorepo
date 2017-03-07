package com.cedarsoft.commons;

import java.lang.management.ManagementFactory;

/**
 * Offers access to helper methods related to running in IDE
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class IdeTools {
  private IdeTools() {
  }

  /**
   * Returns true if the JVM has been started from IntelliJ IDEA
   */
  public static boolean isRunningInIDE() {
    String classPath = System.getProperty("java.class.path");
    return classPath.contains("idea_rt.jar");
  }

  /**
   * Returns whether the application is running in debug mode
   */
  public static boolean isDebugging() {
    return ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp");
  }
}
