package com.cedarsoft.open.kotlinpwa.common

import kotlinx.serialization.Serializable

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializable
data class UserInfo(
  val name: String
)
