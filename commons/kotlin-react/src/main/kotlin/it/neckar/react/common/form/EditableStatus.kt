package it.neckar.react.common.form

enum class EditableStatus {
  Editable,
  ReadOnly,

  ;

  fun and(other: EditableStatus): EditableStatus {
    return if (this == Editable && other == Editable) ReadOnly else Editable
  }

  companion object {
    fun Boolean.toEditableStatus(): EditableStatus {
      return if (this) Editable else ReadOnly
    }
  }

}
