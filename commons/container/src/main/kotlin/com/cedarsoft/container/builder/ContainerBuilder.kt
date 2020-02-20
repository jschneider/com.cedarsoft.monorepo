package com.cedarsoft.container.builder

import com.cedarsoft.container.ContainerEntryDescriptor
import java.io.OutputStream

/**
 * Writes the content for an entry to the given output stream
 */
typealias EntryContentProvider = (OutputStream) -> Unit

/**
 * Creates a new container file
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
interface ContainerBuilder : AutoCloseable {

  /**
   * Adds an entry to the container
   */
  fun addEntry(descriptor: ContainerEntryDescriptor, contentProvider: EntryContentProvider)

  /**
   * Adds an entry to the container with the given content
   */
  fun addEntry(descriptor: ContainerEntryDescriptor, content: ByteArray) {
    addEntry(descriptor) {
      it.write(content)
    }
  }

  /**
   * Adds a string content
   */
  fun addEntry(descriptor: ContainerEntryDescriptor, content: String) {
    addEntry(descriptor, content.toByteArray(Charsets.UTF_8))
  }
}
