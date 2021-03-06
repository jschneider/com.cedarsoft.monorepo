package com.cedarsoft.execution

import java.io.OutputStream

/**
 * Writes output to an output stream
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class OutputStreamByteSink(private val target: OutputStream) : OutputRedirector.ByteSink {
  override fun take(bytes: ByteArray, length: Int) {
    target.write(bytes, 0, length)
  }
}
