package com.cedarsoft.common.kotlin.lang

import kotlinx.browser.window

/**
 * Contains base64 related methods
 *
 *
 * btoa(): Encode [https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/btoa]
 * atob(): Decode [https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/atob]
 *
 */
actual fun ByteArray.toBase64(): String {
  //binary string to base 64
  return window.btoa(this.contentToString())
}

actual fun String.fromBase64(): ByteArray {
  val binaryString = window.atob(this)
  return binaryString.encodeToByteArray()
}


