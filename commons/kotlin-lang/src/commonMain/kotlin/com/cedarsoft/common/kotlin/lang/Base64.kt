package com.cedarsoft.common.kotlin.lang

/**
 * Converts a string to base64
 */
fun String.toBase64(): String {
  return this.encodeToByteArray().toBase64()
}

/**
 * Converts a byte array to base64 - using platform dependent implementation
 */
expect fun ByteArray.toBase64(): String

/**
 * Converts a base64 encoded string to byte array - using platform dependent implementation
 */
expect fun String.fromBase64(): ByteArray

