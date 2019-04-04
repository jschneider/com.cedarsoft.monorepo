package com.cedarsoft.tests.kotlinjs.lib

import kotlin.js.Date

actual fun currentTimeMillis(): Long {
  return Date.now().toLong()
}
