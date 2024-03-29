package it.neckar.open.container.builder

import it.neckar.open.container.ContainerEntryDescriptor
import it.neckar.open.io.NonClosableOutputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import java.io.OutputStream

/**
 * A container that creates a zip file
 */
class ZippedContainerBuilder(val out: OutputStream) : ContainerBuilder {
  private val zipOut = ZipArchiveOutputStream(out)

  override fun addEntry(descriptor: ContainerEntryDescriptor, contentProvider: EntryContentProvider) {
    zipOut.putArchiveEntry(ZipArchiveEntry(descriptor.path))
    contentProvider(NonClosableOutputStream(zipOut))
    zipOut.closeArchiveEntry()
  }

  override fun close() {
    zipOut.flush()
    zipOut.close()
    out.close()
  }
}
