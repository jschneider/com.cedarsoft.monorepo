package com.cedarsoft.execution

import java.io.OutputStream

/**
 * Writes output to an output stream
 */
class OutputStreamByteSink(private val target: OutputStream) : OutputRedirector.ByteSink {
  override fun take(bytes: ByteArray, length: Int) {
    target.write(bytes, 0, length)
  }
}
