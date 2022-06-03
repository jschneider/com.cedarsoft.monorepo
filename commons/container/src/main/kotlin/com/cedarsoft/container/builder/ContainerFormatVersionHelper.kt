package com.cedarsoft.container.builder

import com.cedarsoft.container.ContainerEntryDescriptor
import com.cedarsoft.version.Version
import com.google.common.io.ByteStreams

/**
 * Version number support for container
 */
object ContainerFormatVersionHelper {
  /**
   * The version number of the container format itself
   */
  val containerFormatVersion: Version = Version(0, 0, 1)

  /**
   * The entry descriptor that is used to store the version number
   */
  val versionDescriptor: ContainerEntryDescriptor = ContainerEntryDescriptor("meta/version", Version._1_0_0)

  /**
   * Adds the version number entry to the given container builder
   */
  fun ContainerBuilder.addContainerVersionNumber(version: Version = containerFormatVersion) {
    addEntry(versionDescriptor, version.format())
  }

  fun ZippedContainerReader.verifyContainerVersionNumber() {
    this.nextEntry { descriptor, inputStream ->
      require(versionDescriptor == descriptor) {
        "Unexpected entry found. Was <$descriptor> but expected <$versionDescriptor>"
      }

      val content = ByteStreams.toByteArray(inputStream).toString(Charsets.UTF_8)
      val version = Version.parse(content)

      if (version != containerFormatVersion) {
        throw UnsupportedContainerFormatVersionException(version)
      }

    }
  }
}

/**
 * Is thrown if the container verison is not supported
 */
class UnsupportedContainerFormatVersionException(
  /**
   * The version number of the container
   */
  val containerVersion: Version
) : Exception("Unsupported container version <$containerVersion>") {
}
