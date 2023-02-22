package it.neckar.react.common.loading

import it.neckar.react.common.*
import react.*
import react.dom.*
import styled.*
import kotlinx.css.*
import kotlinx.html.classes


fun RBuilder.bootstrapProgressBar(
  title: String,
  progressType: ProgressType,
  currentPercentage: Int,
  headerProgressBar: Boolean = false,
): Unit = child(bootstrapProgressBar) {
  attrs {
    this.title = title
    this.progressType = progressType
    this.currentPercentage = currentPercentage
    this.headerProgressBar = headerProgressBar
  }
}

val bootstrapProgressBar: FC<ProgressProps> = fc("bootstrapToast") { props ->
  styledDiv {
    attrs {
      classes = setOf("progress")
    }
    css {
      if (props.headerProgressBar) height = 5.px
    }
    styledDiv {
      attrs {
        classes = setOf("progress-bar")
        when (props.progressType) {
          ProgressType.Info -> addClass("bg-info")
          ProgressType.Warning -> addClass("bg-warning")
          ProgressType.Danger -> addClass("bg-danger")
          ProgressType.Success -> addClass("bg-success")
        }
        roleHTML = "progressbar"
        ariaValueRange = IntRange(0, 100)
        ariaValueNow = props.currentPercentage
        ariaLabel = props.title
      }
      css {
        width = props.currentPercentage.pct
        if (props.headerProgressBar) height = 5.px
      }
    }

  }
}

enum class ProgressType {
  Info,
  Warning,
  Danger,
  Success,
}



external interface ProgressProps : Props {
  var title: String
  var progressType: ProgressType
  var currentPercentage: Int
  var headerProgressBar: Boolean
}
