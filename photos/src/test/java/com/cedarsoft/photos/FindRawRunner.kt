package com.cedarsoft.photos

import com.google.common.io.Files
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileFilter
import java.io.FileNotFoundException
import java.lang.IllegalArgumentException

/**
 * Finds the corresponding raw files for jpgs within a directory
 */

fun main(args: Array<String>) {
  val jpgDir = File("/home/johannes/Desktop/waterworld/unprepared")
  ensureDirectory(jpgDir)

  val rawSource = File("/tmp/a/raw")
  ensureDirectory(rawSource)

  val rawTarget = File("/home/johannes/Desktop/waterworld/unprepared/raw")
  ensureDirectory(rawTarget)


  jpgDir.listFiles(FileFilter {
    val name = it.name
    name.endsWith("JPG")
  })
    .forEach {
      val raw = findRaw(it.name, rawSource)
      println("found raw <$raw>")
      FileUtils.copyToDirectory(raw, rawTarget)
    }


}

private fun ensureDirectory(jpgDir: File) {
  if (jpgDir.isDirectory.not()) {
    throw IllegalArgumentException("""Invalid dir <${jpgDir.absolutePath}>""")
  }
}

/**
 * Returns the raw file for a given jpg
 */
fun findRaw(jpgName: String, rawDirectory: File): File {
  val rawFileName = jpgName.substring(0, jpgName.length - 4) + ".CR2"
  val rawFile = File(rawDirectory, rawFileName)

  if (rawFile.exists().not()) {
    throw FileNotFoundException("Could not find <${rawFile.absolutePath}> for <$jpgName>")
  }

  return rawFile
}

class FindRawRunner {

}
