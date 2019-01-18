package com.cedarsoft.io

import java.io.File
import java.io.FileNotFoundException
import java.io.FilterOutputStream
import java.io.IOException
import java.nio.file.Files

/**
 * Writes to a temporary file and moves to the target file name on [close]
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class FileOutputStreamWithMove @Throws(FileNotFoundException::class)
constructor(private val file: File) : FilterOutputStream(null) {
  /**
   * Whether the stream has been closed already
   */
  private var closed: Boolean = false
  /**
   * The tmp file that is written first
   */
  val tmpFile: File

  init {
    tmpFile = File(file.parent, file.name + SUFFIX_TMP + "_" + System.nanoTime())
    tmpFile.deleteOnExit()

    this.out = tmpFile.outputStream().buffered()
  }

  @Throws(IOException::class)
  override fun close() {
    super.close()

    if (closed) {
      return
    }

    //Only move the file if it exists
    if (tmpFile.exists()) {
      //delete the original file first - overwrite mode
      if (file.exists()) {
        file.delete()
      }

      Files.move(tmpFile.toPath(), file.toPath())
    }
    closed = true
  }

  companion object {
    /**
     * The suffix for the tmp file
     */
    val SUFFIX_TMP = ".tmp"
  }
}

/**
 * Creates a new file input stream that first writes to a tmp file and moves the file on close
 */
fun File.outputStreamWithMove(): FileOutputStreamWithMove {
  return FileOutputStreamWithMove(this)
}
