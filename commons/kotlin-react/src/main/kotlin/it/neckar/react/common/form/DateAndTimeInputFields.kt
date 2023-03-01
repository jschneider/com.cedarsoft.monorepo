package it.neckar.react.common.form

import it.neckar.open.kotlin.lang.nextMultipleOf
import it.neckar.open.unit.si.ms
import it.neckar.react.common.*
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import kotlin.js.Date

fun RBuilder.datePicker(
  initialValue: @ms Double,
  /**
   * Is state instance to be updated
   */
  timeState: StateInstance<@ms Double>,

  /**
   * Configuration for the input field
   */
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(datePicker) {

  attrs {
    this.initialValue = initialValue
    this.timeState = timeState
    this.config = config
  }
}

/**
 * A date picker
 */
val datePicker: FC<DatePickerProps> = fc("datePicker") { props ->

  val dateString = Date(props.initialValue).let { date ->
    "${
      date.getFullYear()
    }-${
      "${date.getMonth().plus(1)}".padStart(2, '0')
    }-${
      "${date.getDate()}".padStart(2, '0')
    }"
  }

  input(type = InputType.date, classes = "form-control datepicker") {
    attrs {
      value = dateString
      onChangeFunction = {
        val updatedDate = (it.target as HTMLInputElement).value
        // Automatically update the value in element
        props.timeState.setter(Date.parse(updatedDate))
      }
    }

    props.config?.invoke(this)
  }

}

fun RBuilder.datePickerNullable(
  initialValue: @ms Double?,
  /**
   * Is state instance to be updated
   */
  timeState: StateInstance<@ms Double?>,

  /**
   * Configuration for the input field
   */
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(datePickerNullable) {

  attrs {
    this.initialValue = initialValue
    this.timeState = timeState
    this.config = config
  }
}

/**
 * A date picker
 */
val datePickerNullable: FC<DatePickerNullableProps> = fc("datePickerNullable") { props ->

  val dateString = props.initialValue.let {
    val date = it?.let { Date(it) }

    "${
      date?.getFullYear()
    }-${
      "${date?.getMonth()?.plus(1)}".padStart(2, '0')
    }-${
      "${date?.getDate()}".padStart(2, '0')
    }"
  }

  input(type = InputType.date, classes = "form-control datepicker") {
    attrs {
      value = dateString
      onChangeFunction = {
        val updatedDate = (it.target as HTMLInputElement).value
        // Automatically update the value in element
        props.timeState.setter(Date.parse(updatedDate))
      }
    }

    props.config?.invoke(this)
  }

}


fun RBuilder.timePicker(
  initialValue: @ms Double?,
  /**
   * Is state instance to be updated
   */
  timeState: StateInstance<@ms Double?>,

  timeStep: Int = 15,

  /**
   * Configuration for the input field
   */
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(timePicker) {

  attrs {
    this.initialValue = initialValue
    this.timeState = timeState
    this.timeStep = timeStep
    this.config = config
  }
}

val timePicker: FC<TimePickerProps> = fc("timePicker") { props ->

  val timeString = props.initialValue.let {
    val date = it?.let { Date(it) }
    val minutes = date?.let { (date.getMinutes() + 1).nextMultipleOf(props.timeStep) }

    "${
      "${(date?.getHours()?.plus(if (minutes == 60) 1 else 0))?.rem(24)}".padStart(2, '0')
    }:${
      "${minutes?.rem(60)}".padStart(2, '0')
    }"
  }

  input(type = InputType.time, classes = "form-control datepicker") {
    attrs {
      value = timeString
      step = (props.timeStep * 60).toString()
      onChangeFunction = {
        val updatedValue = (it.target as HTMLInputElement).value
        val updatedValues = updatedValue.split(":").map { it.toInt() }
        val updatedDate = Date(0, 0, 0, updatedValues[0], updatedValues[1])
        // Automatically update the value in element
        props.timeState.setter(updatedDate.getTime())
      }
    }

    props.config?.invoke(this)
  }

}


fun RBuilder.dateTimePickerNullable(
  initialValue: @ms Double?,
  /**
   * Is state instance to be updated
   */
  dateTimeState: StateInstance<@ms Double?>,

  /**
   * Configuration for the input field
   */
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(dateTimePickerNullable) {

  attrs {
    this.initialValue = initialValue
    this.dateTimeState = dateTimeState
    this.config = config
  }
}

val dateTimePickerNullable: FC<DateTimePickerProps> = fc("dateTimePickerNullable") { props ->

  val dateTimeString = props.initialValue.let {
    val date = it?.let { Date(it) }
    val minutes = date?.let { date.getMinutes() }

    "${
      date?.getFullYear() ?: "--"
    }-${
      "${date?.getMonth()?.plus(1)}".padStart(2, '0')
    }-${
      "${date?.getDate()}".padStart(2, '0')
    } ${
      "${date?.getHours()?.rem(24)}".padStart(2, '0')
    }:${
      "${minutes?.rem(60)}".padStart(2, '0')
    }"
  }

  input(type = InputType.dateTimeLocal, classes = "form-control datepicker") {
    attrs {
      value = dateTimeString
      onChangeFunction = {
        val updatedValue = (it.target as HTMLInputElement).value
        // Automatically update the value in element
        props.dateTimeState.setter(Date.parse(updatedValue))
      }
    }

    props.config?.invoke(this)
  }

}


/**
 * Properties for date picker
 */
external interface DatePickerProps : Props {
  var initialValue: @ms Double

  /**
   * The state instance to be changed
   */
  var timeState: StateInstance<@ms Double>

  /**
   * The configuration for the input field
   */
  var config: ((RDOMBuilder<INPUT>) -> Unit)?
}

external interface DatePickerNullableProps : Props {
  var initialValue: @ms Double?

  /**
   * The state instance to be changed
   */
  var timeState: StateInstance<@ms Double?>

  /**
   * The configuration for the input field
   */
  var config: ((RDOMBuilder<INPUT>) -> Unit)?
}

external interface TimePickerProps : Props {
  var initialValue: @ms Double?

  /**
   * The state instance to be changed
   */
  var timeState: StateInstance<@ms Double?>

  var timeStep: Int

  /**
   * The configuration for the input field
   */
  var config: ((RDOMBuilder<INPUT>) -> Unit)?
}

external interface DateTimePickerProps : Props {
  var initialValue: @ms Double?

  /**
   * The state instance to be changed
   */
  var dateTimeState: StateInstance<@ms Double?>

  /**
   * The configuration for the input field
   */
  var config: ((RDOMBuilder<INPUT>) -> Unit)?
}
