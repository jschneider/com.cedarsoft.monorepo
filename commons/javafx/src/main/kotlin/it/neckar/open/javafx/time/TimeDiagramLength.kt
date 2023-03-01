package it.neckar.open.javafx.time

import it.neckar.open.unit.si.s

/**
 */
enum class TimeDiagramLength(
  val seconds: @s Int,
) {
  SECONDS_15(15), SECONDS_30(30), SECONDS_60(60);
}
