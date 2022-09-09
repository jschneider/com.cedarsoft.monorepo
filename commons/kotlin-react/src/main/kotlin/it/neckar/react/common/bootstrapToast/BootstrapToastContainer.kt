package it.neckar.react.common.bootstrapToast



import com.cedarsoft.common.kotlin.lang.percent
import csstype.vw
import it.neckar.react.common.*
import kotlinx.css.*
import react.*
import react.dom.*
import styled.*
import kotlinx.html.classes


val bootstrapToastContainer: FC<ToastProps> = fc("bootstrapToastContainer") { props ->
  styledDiv {
    attrs {
      classes = setOf("fixed-bottom", "d-flex", "flex-column", "align-items-center", "mb-5")
    }
    css {
      zIndex = 2000
    }

    bootstrapToast("basic info toast", ToastType.Info)
    bootstrapToast("basic warning toast", ToastType.Warning)
    bootstrapToast("basic success toast", ToastType.Success)
    bootstrapToast("basic danger toast", ToastType.Danger)
  }
}

