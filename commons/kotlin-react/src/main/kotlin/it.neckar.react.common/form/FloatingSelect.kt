package it.neckar.react.common.form

import com.cedarsoft.common.kotlin.lang.withNullAtFirst
import it.neckar.react.common.*
import it.neckar.uuid.HasUuid
import kotlinx.html.SELECT
import kotlinx.html.id
import react.*
import react.dom.*

/**
 * Creates a floating select field for
 */
fun <T> RBuilder.floatingSelect(
  valueAndSetter: StateInstance<T>,
  idProvider: (T?) -> String,
  formatter: (T) -> String,
  optionClasses: (T) -> Set<String> = { emptySet() },
  availableOptions: List<T>,

  fieldName: String,
  title: String,
  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelect(
    selectedValue = valueAndSetter.value,
    onChange = { valueAndSetter.setter.invoke(it) },
    availableOptions = availableOptions,
    formatter = formatter,
    optionClasses = optionClasses,
    idProvider = idProvider,
    fieldName = fieldName,
    title = title,
    config = config
  )
}

/**
 * Creates a floating select field for [HasUuid] elements
 */
fun <T : HasUuid> RBuilder.floatingSelect(
  valueAndSetter: StateInstance<T>,
  formatter: (T) -> String,
  optionClasses: (T) -> Set<String> = { emptySet() },
  availableOptions: List<T>,
  fieldName: String,
  title: String,
  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  this.floatingSelect(
    valueAndSetter = valueAndSetter,
    idProvider = {
      //Do *NOT* use callback, since this method may be called from another component within a condition
      (it as HasUuid).uuid.toString()
    },
    formatter = formatter,
    optionClasses = optionClasses,
    availableOptions = availableOptions,
    fieldName = fieldName,
    title = title,
    config = config
  )
}

fun <T> RBuilder.floatingSelectNullable(
  valueAndSetter: StateInstance<T?>,
  idProvider: (T?) -> String,
  formatter: (T?) -> String,
  optionClasses: (T?) -> Set<String> = { emptySet() },
  availableOptionsWithoutNull: List<T>,

  fieldName: String,
  title: String,
  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelectNullable(
    selectedValue = valueAndSetter.value,
    onChange = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      valueAndSetter.setter.invoke(it)
    },
    availableOptionsWithoutNull = availableOptionsWithoutNull,
    formatter = formatter,
    optionClasses = optionClasses,
    idProvider = idProvider,
    fieldName = fieldName,
    title = title,
    config = config
  )
}

fun <T : HasUuid> RBuilder.floatingSelectNullable(
  valueAndSetter: StateInstance<T?>,
  formatter: (T?) -> String,
  optionClasses: (T?) -> Set<String> = { emptySet() },
  availableOptionsWithoutNull: List<T>,

  fieldName: String,
  title: String,
  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelectNullable(
    valueAndSetter = valueAndSetter,
    idProvider = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      (it as HasUuid).uuid.toString()
    },
    availableOptionsWithoutNull = availableOptionsWithoutNull,
    formatter = formatter,
    optionClasses = optionClasses,
    fieldName = fieldName,
    title = title,
    config = config
  )
}

fun <E : Enum<E>> RBuilder.floatingSelectEnum(
  valueAndSetter: StateInstance<E>,
  formatter: (E) -> String,
  optionClasses: (E) -> Set<String> = { emptySet() },
  availableOptions: List<E>,

  fieldName: String,
  title: String,
  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelect(
    selectedValue = valueAndSetter.value,
    onChange = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      valueAndSetter.setter.invoke(it)
    },
    availableOptions = availableOptions,
    formatter = formatter,
    optionClasses = optionClasses,
    idProvider = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      it.name
    },
    fieldName = fieldName,
    title = title,
    config = config
  )
}

fun <T> RBuilder.floatingSelect(
  selectedValue: T,
  onChange: OnChange<T>,
  availableOptions: List<T>,
  formatter: (T) -> String,
  optionClasses: (T) -> Set<String> = { emptySet() },

  idProvider: (T) -> String,

  fieldName: String,
  title: String,
  config: ((RDOMBuilder<SELECT>) -> Unit)? = null,
): Unit = child(floatingSelect) {
  require(availableOptions.isNotEmpty()) {
    "availableOptions must not be empty"
  }

  attrs {
    this.selectedValue = selectedValue
    this.onChange = onChange as OnChange<Any?>
    this.formatter = formatter as (Any?) -> String
    this.optionClasses = optionClasses as (Any?) -> Set<String>
    this.idProvider = idProvider as (Any?) -> String
    this.availableOptions = availableOptions as List<Any>
    this.fieldName = fieldName
    this.title = title
    this.config = config
  }
}

/**
 * Floating select that supports null values.
 *
 * This method automatically adds null to the available options
 */
fun <T> RBuilder.floatingSelectNullable(
  selectedValue: T?,
  onChange: OnChange<T?>,
  availableOptionsWithoutNull: List<T>,
  formatter: (T?) -> String,
  optionClasses: (T?) -> Set<String> = { emptySet() },

  idProvider: (T) -> String,

  fieldName: String,
  title: String,
  config: ((RDOMBuilder<SELECT>) -> Unit)? = null,
): Unit = child(floatingSelect) {
  attrs {
    this.selectedValue = selectedValue
    this.onChange = onChange as OnChange<Any?>
    this.formatter = formatter as (Any?) -> String
    this.optionClasses = optionClasses as (Any?) -> Set<String>
    this.idProvider = idProvider as (Any?) -> String

    val optionsIncludingNull = useMemo(availableOptionsWithoutNull) {
      availableOptionsWithoutNull.withNullAtFirst()
    }

    this.availableOptions = optionsIncludingNull as List<Any>
    this.fieldName = fieldName
    this.title = title
    this.config = config
  }
}

val floatingSelect: FunctionComponent<FloatingSelectProps> = fc("floatingSelect") { props ->
  val uniqueId = uniqueIdMemo(props.fieldName)

  //Enforce calling onChange if the current value is *not* in available options
  val availableOptions = props.availableOptions
  val selectedValue = props.selectedValue

  require(availableOptions.contains(selectedValue)) {
    buildString {
      append("selected value (${selectedValue.toString()}) must be in list of available options.\n")
      append("Available options: \n")
      append(availableOptions.joinToString("\n"))
    }
  }

  div("form-floating") {
    select(
      selectedValue = selectedValue,
      onChange = props.onChange,
      availableOptions = availableOptions,
      formatter = props.formatter,
      optionClasses = props.optionClasses,
      idProvider = props.idProvider,
      fieldName = props.fieldName,
      title = props.title
    ) {
      attrs {
        id = uniqueId
      }

      props.config?.invoke(it)
    }

    label("form-label") {
      +props.title

      attrs {
        htmlFor = uniqueId
      }
    }
  }
}

external interface FloatingSelectProps : Props {
  /**
   * The selected value
   */
  var selectedValue: Any?
  var formatter: (Any?) -> String
  var optionClasses: (Any?) -> Set<String>

  var onChange: OnChange<Any?>
  var availableOptions: List<Any?>
  var idProvider: (Any?) -> String

  var fieldName: String
  var title: String

  var config: ((RDOMBuilder<SELECT>) -> Unit)?
}
