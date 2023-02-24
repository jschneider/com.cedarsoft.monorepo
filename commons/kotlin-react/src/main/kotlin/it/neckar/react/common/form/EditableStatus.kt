package it.neckar.react.common.form

enum class EditableStatus {
  Editable,
  ReadOnly,

  ;

  fun and(other: EditableStatus): EditableStatus {
    return if (this == Editable && other == Editable) Editable else ReadOnly
  }

  fun and(other: Boolean): EditableStatus {
    return if (this == Editable && other) Editable else ReadOnly
  }

  fun or(other: EditableStatus): EditableStatus {
    return if (this == Editable || other == Editable) Editable else ReadOnly
  }

  fun or(other: Boolean): EditableStatus {
    return if (this == Editable || other) Editable else ReadOnly
  }

  companion object {
    fun Boolean.toEditableStatus(): EditableStatus {
      return if (this) Editable else ReadOnly
    }
  }

}
