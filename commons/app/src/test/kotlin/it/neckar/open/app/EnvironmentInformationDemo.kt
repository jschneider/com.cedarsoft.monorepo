package it.neckar.open.app

/**
 */
internal fun main(args: Array<String>) {
  println("Hostname: ${EnvironmentInformation.hostName}")
  println("UserName: ${EnvironmentInformation.userName}")
  println("Environment Description: ${EnvironmentInformation.createEnvironmentDescription()}")
  println("Host Description: ${EnvironmentInformation.createHostDescription()}")
}
