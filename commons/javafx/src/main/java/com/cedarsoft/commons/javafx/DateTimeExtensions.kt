package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.time.DateUtils
import javafx.beans.binding.Bindings
import javafx.beans.binding.StringBinding
import javafx.beans.value.ObservableValue
import java.time.Duration
import java.time.LocalTime
import java.time.Period
import java.time.temporal.Temporal
import java.util.concurrent.Callable

/**
 * Extension methods for Date and time related stuff
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

/**
 * Format in HH:mm format
 */
fun ObservableValue<LocalTime?>.asHHmm(): StringBinding {
  return Bindings.createStringBinding(Callable {
    this.value?.asHHmm() ?: ""
  }, this)
}

fun LocalTime.asHHmm(): String {
  return DateUtils.formatHHmm(this)
}


fun ObservableValue<Duration?>.asWords(): StringBinding {
  return Bindings.createStringBinding(Callable {
    this.value?.asWords() ?: ""
  }, this)
}


fun Duration.asWords(): String {
  return DateUtils.formatDurationWords(this)
}

fun Duration.asHHmm(): String {
  return DateUtils.formatDurationHHmm(this)
}

fun Temporal.formatLocalDateAndOrTime(): String {
  return DateUtils.formatLocalDateAndOrTime(this)
}


fun ObservableValue<Period?>.asWeeksAndDays(): StringBinding {
  return Bindings.createStringBinding(Callable {
    this.value?.asWeeksAndDays() ?: ""
  }, this)
}

fun Period.asWeeksAndDays(): String {
  return DateUtils.asWeeksAndDays(this)
}




