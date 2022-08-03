package it.neckar.react.common.bootstrapToast


import it.neckar.react.common.*
import react.*
import react.dom.*
import styled.*
import kotlinx.css.*
import kotlinx.html.classes


fun RBuilder.bootstrapToast(
  title: String,
  toastType: ToastType,
): Unit = child(bootstrapToast) {
  attrs {
    this.title = title
    this.toastType = toastType
  }
}


val bootstrapToast: FC<ToastProps> = fc("bootstrapToast") { props ->
  styledDiv {
    attrs {
      classes = setOf("alert", "d-flex", "align-items-center", "p-2", "my-1")
      when (props.toastType) {
        ToastType.Info -> addClass("alert-info")
        ToastType.Warning -> addClass("alert-warning")
        ToastType.Danger -> addClass("alert-danger")
        ToastType.Success -> addClass("alert-success")
      }
    }
    css {
      width = 250.px
    }

    i(classes = "mx-2") {
      attrs {
        when(props.toastType) {
          ToastType.Info -> addClass("fas fa-info-circle")
          ToastType.Warning -> addClass("fas fa-exclamation-triangle")
          ToastType.Danger -> addClass("fas fa-exclamation-circle")
          ToastType.Success -> addClass("fas fa-check-circle")
        }
      }
    }

    small {
      +props.title
    }
  }
}

enum class ToastType {
  Info,
  Warning,
  Danger,
  Success,
}


external interface ToastProps : Props {
  var title: String
  var toastType: ToastType
}
