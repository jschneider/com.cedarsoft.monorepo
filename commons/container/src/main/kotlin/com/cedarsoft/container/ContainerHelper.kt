package com.cedarsoft.container

import com.google.common.io.ByteStreams
import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import java.io.InputStream
import java.io.StringWriter
import java.nio.charset.Charset
import javax.annotation.Nonnull

/**
 * Contains helper methods for a container
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
object ContainerHelper {
  /**
   * Lists the paths of the zip file
   */
  fun getZipToc(containerIn: InputStream): String {
    @Nonnull val out = StringWriter()

    ZipArchiveInputStream(containerIn).use { zipIn ->
      var entry: ArchiveEntry? = zipIn.nextZipEntry

      while (entry != null) {
        val content = ByteStreams.toByteArray(zipIn)
        out.write("${entry.name} (${content.size} byte)\n")
        entry = zipIn.nextZipEntry
      }
    }

    return out.toString()
  }

  /**
   * Returns the content of one entry of the container zip
   */
  fun getContent(containerIn: InputStream, path: String): ByteArray {
    ZipArchiveInputStream(containerIn).use { zipIn ->
      var entry: ArchiveEntry? = zipIn.nextZipEntry

      while (entry != null) {
        val currentDescriptor = ContainerEntryDescriptor.fromPath(entry.name)

        if (currentDescriptor.path == path) {
          return ByteStreams.toByteArray(zipIn)
        }

        entry = zipIn.nextZipEntry
      }
    }

    throw IllegalArgumentException("No entry found for path <$path>")
  }

  /**
   * Returns the content as string
   */
  fun getContentAsString(containerIn: InputStream, path: String, charset: Charset = Charsets.UTF_8): String {
    return getContent(containerIn, path).toString(charset)
  }
}
