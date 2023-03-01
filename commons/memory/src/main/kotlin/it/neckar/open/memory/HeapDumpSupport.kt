package it.neckar.open.memory

import com.sun.management.HotSpotDiagnosticMXBean
import java.lang.management.ManagementFactory
import javax.annotation.Nonnull

/**
 * Supports creating heap dumps using MBean
 */
object HeapDumpSupport {
  // This is the name of the HotSpot Diagnostic MBean
  private const val HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic"

  fun dumpHeap(@Nonnull targetFileName: String, toDump: ObjectsToDump = ObjectsToDump.Live) {
    val hotSpotMBean = hotSpotMBean
    hotSpotMBean.dumpHeap(targetFileName, toDump == ObjectsToDump.Live)
  }

  /**
   * Returns the hot spot MBean
   */
  private val hotSpotMBean: HotSpotDiagnosticMXBean
    get() {
      return ManagementFactory.newPlatformMXBeanProxy(ManagementFactory.getPlatformMBeanServer(), HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean::class.java)
    }
}

enum class ObjectsToDump {
  /**
   * Only the live objects that are reachable
   */
  Live,

  /**
   * Dumps all objects - even non reachable objects
   */
  All,
}
