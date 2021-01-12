package com.cedarsoft.common.kotlin.lang

import java.util.Base64


actual fun ByteArray.toBase64(): String {
  return Base64.getEncoder().encode(this).decodeToString()
}

actual fun String.fromBase64(): ByteArray {
  return Base64.getDecoder().decode(this)
}
