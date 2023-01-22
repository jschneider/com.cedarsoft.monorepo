package it.neckar.react.common.form

import it.neckar.react.common.*
import it.neckar.react.common.form.EditableStatus.*
import it.neckar.uuid.HasUuid
import kotlinx.html.SELECT
import kotlinx.html.js.onChangeFunction
import kotlinx.html.title
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.*

/**
 * A select box
 */
fun <T : HasUuid> RBuilder.selectHasUuid(
  /**
   * The selected value
   */
  selectedValue: T,
  onChange: OnChange<T>,

  availableOptions: List<T>,
  formatter: (T) -> String,

  fieldName: String,
  title: String,

  editableStatus: EditableStatus,

  config: ((RDOMBuilder<SELECT>) -> Unit)?,

  ): Unit = child(select) {
  attrs {
    this.selectedValue = selectedValue
    this.formatter = formatter.unsafeCast<(Any?) -> String>()
    this.onChange = onChange.unsafeCast<OnChange<Any?>>()
    this.idProvider = { (it as HasUuid).toString() }
    this.availableOptions = availableOptions
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.config = config
  }
}

/**
 * A select field for an enum value
 */
fun <E : Enum<E>> RBuilder.selectEnum(
  /**
   * The selected value
   */
  selectedValue: E,
  onChange: OnChange<E>,

  availableOptions: List<E>,
  formatter: (E) -> String,

  fieldName: String,
  title: String,

  editableStatus: EditableStatus,

  config: ((RDOMBuilder<SELECT>) -> Unit)?,

  ): Unit = child(select) {
  attrs {
    this.selectedValue = selectedValue
    this.formatter = formatter.unsafeCast<(Any?) -> String>()
    this.onChange = onChange.unsafeCast<OnChange<Any?>>()
    this.idProvider = useCallback { (it.unsafeCast<E>()).name }
    this.availableOptions = availableOptions
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.config = config
  }
}

fun <E : Enum<E>> RBuilder.selectEnum(
  valueAndSetter: StateInstance<E>,
  formatter: (E) -> String,
  availableOptions: Array<E>,

  fieldName: String,
  title: String,

  additionalOnChange: ((E) -> Unit)? = null,

  editableStatus: EditableStatus,

  config: (RDOMBuilder<SELECT>.() -> Unit)? = null,
) {
  select(
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


/**
 * A select field
 */
fun <T> RBuilder.select(
  /**
   * The selected value
   */
  selectedValue: T,
  onChange: OnChange<T>,

  availableOptions: List<T>,
  formatter: (T) -> String,

  idProvider: (T) -> String,

  fieldName: String,
  title: String,

  editableStatus: EditableStatus,

  config: ((RDOMBuilder<SELECT>) -> Unit)?,

  ): Unit = child(select) {
  attrs {
    this.selectedValue = selectedValue
    this.formatter = formatter.unsafeCast<(Any?) -> String>()
    this.onChange = onChange.unsafeCast<OnChange<Any?>>()
    this.idProvider = idProvider.unsafeCast<(Any) -> String>()
    this.availableOptions = availableOptions
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.config = config
  }
}

/**
 * Identifier that is used for null values
 */
const val NullId: String = "___null-identifier___"

val select: FC<SelectProps> = fc("select") { props ->
  val idProvider = props.idProvider

  select(classes = "form-select") {
    attrs {
      title = props.title
      name = props.fieldName

      val selectedValue = props.selectedValue
      value = if (selectedValue == null) NullId else idProvider(selectedValue)

      disabled = props.editableStatus == ReadOnly

      onChangeFunction = { event ->
        val inputElement = event.target as HTMLSelectElement
        val valueAsString = inputElement.value

        val newSelection = if (valueAsString == NullId) null else props.availableOptions.first {
          it != null && idProvider(it) == valueAsString
        }

        props.onChange(newSelection)
      }
    }

    //Generate the options
    props.availableOptions.forEach { availableOption ->
      option {
        attrs {
          this.label = props.formatter(availableOption)
          this.value = if (availableOption == null) NullId else idProvider(availableOption)
          //Do *NOT* use selected attribute, React prefers the value attribute of the select element
          //this.selected
        }
      }
    }

    props.config?.invoke(this)
  }
}

external interface SelectProps : Props {
  /**
   * The selected value
   */
  var selectedValue: Any?
  var onChange: OnChange<Any?>

  var availableOptions: List<Any?>
  var formatter: (Any?) -> String

  /**
   * Provides a String ID for each element
   * [selectedValue] and [availableOptions]
   */
  var idProvider: (Any) -> String

  var fieldName: String
  var title: String

  var editableStatus: EditableStatus

  var config: ((RDOMBuilder<SELECT>) -> Unit)?
}

