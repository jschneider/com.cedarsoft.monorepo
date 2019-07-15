package com.cedarsoft.open.kotlinpwa.common

import kotlinx.serialization.Serializable

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Serializable
data class PositionInfo(
  val userInfo: UserInfo,
  val coords: Coords,
  val updatedTime: Long
)

