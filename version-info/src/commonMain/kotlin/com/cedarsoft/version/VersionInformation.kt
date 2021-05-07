package com.cedarsoft.version

/**
 * Application related information
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object VersionInformation {
  /**
   * The version number (main version number of the repository)
   */
  val version: String = VersionConstants.monorepoVersion

  /**
   * Returns true if the current version is a snapshot
   */
  val isSnapshot: Boolean = version.isSnapshot()


  /**
   * The build date (only day - not the time)
   */
  val buildDateDay: String = VersionConstants.buildDateDay

  val gitCommit: String = VersionConstants.gitCommit

  /**
   * Verbose version string that contains the git information
   */
  val versionAsStringVerbose: String
    get() {
      return "$version ($gitCommit)"
    }
}

/**
 * Returns true if the given version number string ends with "-SNAPSHOT"
 */
fun String.isSnapshot(): Boolean {
  return this.endsWith("-SNAPSHOT")
}
