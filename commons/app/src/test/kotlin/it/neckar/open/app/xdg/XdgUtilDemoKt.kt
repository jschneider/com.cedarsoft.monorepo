package it.neckar.open.app.xdg

import com.google.common.base.StandardSystemProperty
import org.junit.Test

/**
 */
class XdgUtilDemoKt {
  @Test
  @Throws(Exception::class)
  fun testIt() {
    val value = StandardSystemProperty.OS_NAME.value()
    if (value != null && !value.contains("Linux")) {
      System.err.println("Not running on Linux!")
      return
    }

    System.err.println("Config Home: " + XdgUtil.configHome.absolutePath)
    System.err.println("Cache Home: " + XdgUtil.cacheHome.absolutePath)
    System.err.println("Data Home: " + XdgUtil.dataHome.absolutePath)
  }
}
