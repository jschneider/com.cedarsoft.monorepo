package com.cedarsoft.open.kotlinpwa.common

import kotlinx.serialization.Serializable

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializable
data class PositionReport(
  val updateTime: Long,
  val positionInfos: List<PositionInfo>
) {


  companion object {
    val empty: PositionReport = PositionReport(0, listOf())
  }
}
