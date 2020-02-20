package com.cedarsoft.container

import com.cedarsoft.version.Version

/**
 * Identifies an entry within the container
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
data class ContainerEntryDescriptor(
  /**
   * The id - may contains "/" - is interpreted as directories path
   */
  val id: String,

  /**
   * The version number
   */
  val version: Version
) {
  /**
   * Returns the (zip) path within the container
   */
  val path: String
    get() {
      return "$id$delimiter$version"
    }

  companion object {
    /**
     * The delimiter between id and version
     */
    const val delimiter: Char = '/'

    /**
     * Returns the container entry descriptor from the given path
     */
    fun fromPath(path: String): ContainerEntryDescriptor {
      val lastIndexOf = path.lastIndexOf(delimiter)
      require(lastIndexOf > 0) { "invalid path. No $delimiter found" }
      require(path.last() != delimiter) { "invalid path. Last character is a $delimiter" }

      val id = path.substring(0, lastIndexOf)
      val versionString = path.substring(lastIndexOf + 1)

      return ContainerEntryDescriptor(id, Version.parse(versionString))
    }
  }
}
