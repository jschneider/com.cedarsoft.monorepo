package com.cedarsoft.app

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal fun main(args: Array<String>) {
  println("Hostname: ${EnvironmentInformation.hostName}")
  println("UserName: ${EnvironmentInformation.userName}")
  println("Environment Description: ${EnvironmentInformation.createEnvironmentDescription()}")
  println("Host Description: ${EnvironmentInformation.createHostDescription()}")
}
