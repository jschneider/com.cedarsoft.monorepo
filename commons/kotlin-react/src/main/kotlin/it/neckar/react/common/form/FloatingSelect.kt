package it.neckar.react.common.form

import com.cedarsoft.common.kotlin.lang.withNullAtFirst
import it.neckar.react.common.*
import it.neckar.react.common.form.EditableStatus.*
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
  idProvider: (T) -> String,
  formatter: (T) -> String,
  availableOptions: List<T>,

  fieldName: String,
  title: String,

  additionalOnChange: ((T) -> Unit)? = null,

  editableStatus: EditableStatus = Editable,

  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelect(
    selectedValue = valueAndSetter.value,
    onChange = {
      valueAndSetter.setter.invoke(it)
      additionalOnChange?.invoke(it)
    },
    availableOptions = availableOptions,
    formatter = formatter,
    idProvider = idProvider,
    fieldName = fieldName,
    title = title,
    editableStatus = editableStatus,
    config = config
  )
}

/**
 * Creates a floating select field for [HasUuid] elements
 */
fun <T : HasUuid> RBuilder.floatingSelect(
  valueAndSetter: StateInstance<T>,
  formatter: (T) -> String,
  availableOptions: List<T>,

  fieldName: String,
  title: String,

  additionalOnChange: ((T) -> Unit)? = null,

  editableStatus: EditableStatus = Editable,

  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelect(
    selectedValue = valueAndSetter.value,
    onChange = {
      valueAndSetter.setter.invoke(it)
      additionalOnChange?.invoke(it)
    },
    formatter = formatter,
    availableOptions = availableOptions,
    idProvider = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      (it as HasUuid).uuid.toString()
    },
    fieldName = fieldName,
    title = title,
    editableStatus = editableStatus,
    config = config,
  )
}

fun <T> RBuilder.floatingSelectNullable(
  valueAndSetter: StateInstance<T?>,
  idProvider: (T?) -> String,
  formatter: (T?) -> String,
  availableOptionsWithoutNull: List<T>,

  fieldName: String,
  title: String,

  additionalOnChange: ((T?) -> Unit)? = null,

  editableStatus: EditableStatus = Editable,

  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelectNullable(
    selectedValue = valueAndSetter.value,
    onChange = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      valueAndSetter.setter.invoke(it)
      additionalOnChange?.invoke(it)
    },
    availableOptionsWithoutNull = availableOptionsWithoutNull,
    formatter = formatter,
    idProvider = idProvider,
    fieldName = fieldName,
    title = title,
    editableStatus = editableStatus,
    config = config,
  )
}

fun <T : HasUuid> RBuilder.floatingSelectUuidNullable(
  valueAndSetter: StateInstance<T?>,
  formatter: (T?) -> String,
  availableOptionsWithoutNull: List<T>,

  fieldName: String,
  title: String,

  additionalOnChange: ((T?) -> Unit)? = null,

  editableStatus: EditableStatus = Editable,

  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelectNullable(
    selectedValue = valueAndSetter.value,
    onChange = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      valueAndSetter.setter(it)
      additionalOnChange?.invoke(it)
    },
    availableOptionsWithoutNull = availableOptionsWithoutNull,
    formatter = formatter,
    idProvider = { it.uuid.toString() },
    fieldName = fieldName,
    title = title,
    editableStatus = editableStatus,
    config = config,
  )
}

fun <E : Enum<E>> RBuilder.floatingSelectEnum(
  valueAndSetter: StateInstance<E>,
  formatter: (E) -> String,
  availableOptions: Array<E>,

  fieldName: String,
  title: String,

  additionalOnChange: ((E) -> Unit)? = null,

  editableStatus: EditableStatus,

  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  floatingSelect(
    selectedValue = valueAndSetter.value,
    onChange = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      valueAndSetter.setter.invoke(it)
      additionalOnChange?.invoke(it)
    },
    availableOptions = availableOptions.toList(),
    formatter = formatter,
    idProvider = {
      //Do *NOT* use callback since this method may be called from another component within a condition
      it.name
    },
    fieldName = fieldName,
    title = title,
    editableStatus = editableStatus,
    config = config,
  )
}

fun <T> RBuilder.floatingSelect(
  selectedValue: T,
  onChange: OnChange<T>,
  availableOptions: List<T>,
  formatter: (T) -> String,

  idProvider: (T) -> String,

  fieldName: String,
  title: String,

  editableStatus: EditableStatus = Editable,

  config: ((RDOMBuilder<SELECT>) -> Unit)? = null,
): Unit = child(floatingSelect) {
  require(availableOptions.isNotEmpty()) {
    "availableOptions must not be empty"
  }

  attrs {
    this.selectedValue = selectedValue
    this.onChange = onChange.unsafeCast<OnChange<Any?>>()
    this.formatter = formatter.unsafeCast<(Any?) -> String>()
    this.idProvider = idProvider.unsafeCast<(Any?) -> String>()
    this.availableOptions = availableOptions.unsafeCast<List<Any>>()
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
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

  idProvider: (T) -> String,

  fieldName: String,
  title: String,

  editableStatus: EditableStatus,

  config: ((RDOMBuilder<SELECT>) -> Unit)? = null,
): Unit = child(floatingSelect) {
  attrs {
    this.selectedValue = selectedValue
    this.onChange = onChange.unsafeCast<OnChange<Any?>>()
    this.formatter = formatter.unsafeCast<(Any?) -> String>()
    this.idProvider = idProvider.unsafeCast<(Any?) -> String>()

    val optionsIncludingNull = availableOptionsWithoutNull.withNullAtFirst()

    this.availableOptions = optionsIncludingNull.unsafeCast<List<Any>>()
    this.fieldName = fieldName
    this.title = title

    this.editableStatus = editableStatus

    this.config = config
  }
}

val floatingSelect: FC<FloatingSelectProps> = fc("floatingSelect") { props ->
  val uniqueId = uniqueIdMemo(props.fieldName)

  //Enforce calling onChange if the current value is *not* in available options
  val availableOptions = props.availableOptions
  val selectedValue = props.selectedValue

  require(availableOptions.contains(selectedValue)) {
    buildString {
      append("selected value (${selectedValue.toString()}) must be in list of available options.\n")
      append("Available options:\n")
      append(availableOptions.joinToString("\n"))
    }
  }

  div("form-floating") {
    select(
      selectedValue = selectedValue,
      onChange = props.onChange,
      availableOptions = availableOptions,
      formatter = props.formatter,
      idProvider = props.idProvider,
      fieldName = props.fieldName,
      title = props.title,
      editableStatus = props.editableStatus,
    ) {
      attrs {
        id = uniqueId
      }

      props.config?.invoke(it)
    }

    label("form-label") {
      +props.title

      attrs {
        htmlForFixed = uniqueId
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

  var onChange: OnChange<Any?>
  var availableOptions: List<Any?>
  var idProvider: (Any?) -> String

  var fieldName: String
  var title: String

  var editableStatus: EditableStatus

  var config: ((RDOMBuilder<SELECT>) -> Unit)?
}
