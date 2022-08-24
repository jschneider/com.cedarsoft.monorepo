package com.cedarsoft.io

/**
 * Handles multiple [AutoCloseable]s
 */
class Resources : AutoCloseable {
  private val resources: MutableList<AutoCloseable> = mutableListOf()

  fun <T : AutoCloseable> T.use(action: ((T) -> Unit)? = null): T {
    resources += this
    action?.invoke(this)
    return this
  }

  override fun close() {
    var exception: Exception? = null

    //Close the resources in the reversed order (inner streams first)
    for (resource in resources.reversed()) {
      try {
        resource.close()
      } catch (e: Exception) {
        //add the suppressed exception
        if (exception == null) exception = e else exception.addSuppressed(e)
      }
    }

    exception?.let {
      throw it
    }
  }
}

inline fun <T> withResources(block: Resources.() -> T): T = Resources().use(block)
