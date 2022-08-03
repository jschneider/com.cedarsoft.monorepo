package com.cedarsoft.container.builder

import com.cedarsoft.container.ContainerEntryDescriptor
import com.cedarsoft.io.NonClosableInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import java.io.InputStream


typealias EntryConsumer = (descriptor: ContainerEntryDescriptor, inputStream: InputStream) -> Unit


/**
 * Reads a container
 *
 */
class ZippedContainerReader(val inputStream: InputStream) : AutoCloseable {
  /**
   * The zip input stream that should be used
   */
  private val zipIn = ZipArchiveInputStream(inputStream)

  override fun close() {
    zipIn.close()
    inputStream.close()
  }

  fun nextEntry(consumer: EntryConsumer) {
    val zipEntry: ZipArchiveEntry = checkNotNull(zipIn.nextZipEntry) { "No more entries available" }
    val descriptor = ContainerEntryDescriptor.fromPath(zipEntry.name)

    consumer(descriptor, NonClosableInputStream(zipIn))
  }
}

