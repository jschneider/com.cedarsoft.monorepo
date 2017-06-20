package com.cedarsoft.io

import java.io.File


fun main(args: Array<String>) {
    println("Hello World!")

    val pathFinder = RelativePathFinder()

    val file = File("/tmp/test")
    val file2 = File("/")

    val relativePath = RelativePathFinder.getRelativePath(file, file2)
    println("relative path: " + relativePath)

}

