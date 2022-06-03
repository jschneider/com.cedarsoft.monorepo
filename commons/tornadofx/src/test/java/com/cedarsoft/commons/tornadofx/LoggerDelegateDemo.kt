package com.cedarsoft.commons.tornadofx

import org.slf4j.Logger

/**
 * Shows how to use the logger
 */
internal class LoggerDelegateDemo {
  companion object {
    val logger: Logger by LoggerDelegate()
  }
}

