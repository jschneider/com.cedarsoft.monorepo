package it.neckar.react.common.loading

import com.cedarsoft.unit.other.pct
import com.cedarsoft.unit.other.pct100
import it.neckar.react.common.*
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.classes
import react.*
import react.dom.*
import styled.*
import kotlin.math.roundToInt


/**
 * Bootstrap progress bar with a custom color.
 * If headerProgressBar is true, the progress bar will fade out upon completion and fade in when the current percentage is below 100%.
 * @param title Title of the progress bar. Is used as ariaLabel (for e.g. screen readers).
 * @param customColor enter an additional class to color the progress bar. Should start with 'bg-', e.g. bg-primary
 * @param currentPercentage The to be displayed percentage (0.0 to 1.0)
 * @param headerProgressBar If the progress bar is located in the header it will behave and look differently from the default progress bar.
 */
fun RBuilder.bootstrapProgressBarCustomColor(
  title: String,
  currentPercentage: @pct Double,
  customColor: String? = null,
  headerProgressBar: ProgressBarTyp = ProgressBarTyp.DEFAULT_PROGRESS_BAR,
): Unit = child(bootstrapProgressBarCustomColor) {
  attrs {
    this.title = title
    this.currentPercentage = currentPercentage
    this.headerProgressBar = headerProgressBar
    this.customColor = customColor
  }
}

val bootstrapProgressBarCustomColor: FC<ProgressCustomColorProps> = fc("bootstrapProgressBarCustomColor") { props ->
  val customColor = props.customColor
  @pct val currentPercentage = props.currentPercentage
  @pct100 val currentPercentage100 = (currentPercentage * 100).roundToInt()

  val (visible, setVisible) = useState(true)
  useEffect(props.currentPercentage) {
    setVisible(
      currentPercentage < 1.0
    )
  }

  styledDiv {
    attrs {
      classes = setOf("progress")
    }
    css {// styling of the outer progress div. The outer progress div is the grey background.
      if (props.headerProgressBar == ProgressBarTyp.HEADER_PROGRESS_BAR) height = 5.px
      if (!visible) visibility = Visibility.hidden
    }


    styledDiv {
      attrs {
        classes = if (customColor != null) {
          require(customColor.startsWith("bg-")) {
            "Progress Bar with a custom color requires an additional class which must start with 'bg-'. "
          }
          setOf("progress-bar", customColor)
        } else {
          setOf("progress-bar")
        }

        roleHTML = "progressbar"
        ariaValueRange = IntRange(0, 100)
        ariaValueNow = currentPercentage100
        ariaLabel = props.title
      }

      css {
        width = currentPercentage100.pct

        if (props.headerProgressBar == ProgressBarTyp.HEADER_PROGRESS_BAR){
          height = 5.px

          if (visible) { // transitions to make the header progress bar fade out when at 100 and fade in when at less than 100. The fade out is slow enough so that the user can see the full progress bar.
            visibility = Visibility.visible
            opacity = 1
            this.transition("opacity", (0.1).s, Timing.easeOut, 0.s)

          } else {
            visibility = Visibility.hidden
            opacity = 0
            this.transition("visibility", 1.s, Timing.easeIn, 0.s)
            this.transition("opacity", 1.s, Timing.easeIn, 0.s)
          }
        }else if (props.headerProgressBar == ProgressBarTyp.DEFAULT_PROGRESS_BAR){
          visibility = Visibility.visible
          opacity = 1
        }
      }
    }
  }
}

external interface ProgressCustomColorProps : Props {
  var title: String
  var currentPercentage: @pct Double
  var headerProgressBar: ProgressBarTyp
  var customColor: String?
}
