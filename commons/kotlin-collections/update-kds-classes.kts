#!/usr/bin/env kscript
//DEPS com.google.guava:guava:28.1-jre

import com.google.common.io.Files
import java.io.File


println("Cloning kds repository")

val projectDir = File(".")
val buildDir = File(projectDir, "build")
val kdsImportDir = File(buildDir, "kds-import")
val kdsImportSourcesDir = File(kdsImportDir, "kds/kds/src")
val commonMainKotlinDir = File(kdsImportSourcesDir, "commonMain/kotlin/")


kdsImportDir.mkdirs()

val process = Runtime.getRuntime().exec(arrayOf("git", "clone", "https://github.com/korlibs/kds.git"), arrayOf(), kdsImportDir)
println("Result: ${process.waitFor()}")

println(process.inputStream.readBytes().toString(Charsets.UTF_8))
println(process.errorStream.readBytes().toString(Charsets.UTF_8))


val targetPackageDir = File(projectDir, "src/commonMain/kotlin/com/cedarsoft/common/collections")


val sourceFilesToCopy = listOf(
  "com/soywiz/kds/_Extensions.kt",
  "com/soywiz/kds/internal/internal.kt",
  "com/soywiz/kds/Queue.kt",
  "com/soywiz/kds/ArrayDeque.kt",
  "com/soywiz/kds/IteratorExt.kt",
  "com/soywiz/kds/RingBuffer.kt",
  "com/soywiz/kds/Deque.kt",
  "com/soywiz/kds/BitSet.kt",
  "com/soywiz/kds/CacheMap.kt",
  "com/soywiz/kds/Pool.kt",
  "com/soywiz/kds/PriorityQueue.kt",
  "com/soywiz/kds/Stack.kt",
  "com/soywiz/kds/IntMap.kt",
  "com/soywiz/kds/ArrayList.kt",
  "com/soywiz/kds/ArrayListExt.kt",
  "com/soywiz/kds/Array2.kt",
  "com/soywiz/kds/_GenericSort.kt"
)

sourceFilesToCopy.forEach {
  println("Copy $it")

  val sourceFile = File(commonMainKotlinDir, it)
  val targetFile = File(targetPackageDir, sourceFile.name)


  val sourceContentOriginal: String = Files.asByteSource(sourceFile).read().toString(Charsets.UTF_8)
  val sourceContent: String = sourceContentOriginal.processForCedarsoft()

  println("Copying:")
  println("\tFROM ${sourceFile.absolutePath}")
  println("\tTO   ${targetFile.absolutePath}")

  Files.asByteSink(targetFile).write(sourceContent.toByteArray(Charsets.UTF_8))
}

fun String.processForCedarsoft(): String {
  return fixPackages()
}

fun String.fixPackages(): String {
  return replace("com.soywiz.kds.internal", "com.cedarsoft.common.collections")
    .replace("com.soywiz.kds", "com.cedarsoft.common.collections")
}
