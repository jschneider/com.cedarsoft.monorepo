package com.cedarsoft.commons.javafx.time

import com.cedarsoft.unit.si.s

/**
 */
enum class TimeDiagramLength(
  val seconds: @s Int,
) {
  SECONDS_15(15), SECONDS_30(30), SECONDS_60(60);
}
