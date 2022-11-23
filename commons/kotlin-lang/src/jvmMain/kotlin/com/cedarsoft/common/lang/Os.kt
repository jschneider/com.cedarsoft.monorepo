package com.cedarsoft.common.lang

import com.cedarsoft.common.kotlin.lang.checkNotNull

/**
 * Allows checks for the operating system
 */
object Os {
  val osName: String = System.getProperty("os.name").checkNotNull { "Property os.name not found" }
  val isLinux: Boolean = osName.contains("Linux") || osName.contains("LINUX")
  val isWindows: Boolean = osName.contains("Windows")
}
