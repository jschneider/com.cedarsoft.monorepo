package com.cedarsoft.io

import java.io.*
import java.nio.file.Files

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class FileOutputStreamWithMove @Throws(FileNotFoundException::class)

constructor(private val file: File) : FilterOutputStream(null) {

    private var closed: Boolean = false
    private val tmpFile: File

    init {

        tmpFile = File(file.parent, file.name + SUFFIX_TMP)
        tmpFile.deleteOnExit()

        this.out = BufferedOutputStream(FileOutputStream(tmpFile))
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
        val SUFFIX_TMP = ".tmp"
    }
}

