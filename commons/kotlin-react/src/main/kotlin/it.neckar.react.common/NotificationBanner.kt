package it.neckar.react.common

import it.neckar.react.common.BannerOrientation.*
import it.neckar.react.common.BannerState.*
import kotlinx.html.DIV
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * Generates the app footer
 */
fun RBuilder.notificationBanner(
  title: String,
  bannerType: BannerType,
  bannerOrientation: BannerOrientation,
  bannerState: StateInstance<BannerState>,
  block: RDOMBuilder<DIV>.() -> Unit,
): Unit = child(notificationBanner) {
  attrs {
    this.title = title
    this.bannerType = bannerType
    this.bannerOrientation = bannerOrientation
    this.bannerState = bannerState
    this.block = block
  }
}

val notificationBanner: FunctionComponent<NotificationBannerProps> = fc("notificationBanner") { props ->
  div(classes = "alert alert-dismissible d-none") {
    attrs {
      when (props.bannerType) {
        BannerType.Info -> addClass("alert-info")
        BannerType.Warning -> addClass("alert-warning")
        BannerType.Danger -> addClass("alert-danger")
        BannerType.Success -> addClass("alert-success")
      }

      addClassIf("fixed-top") {
        props.bannerOrientation == Top
      }
      addClassIf("fixed-bottom") {
        props.bannerOrientation == Bottom
      }
      removeClassIf("d-none") {
        props.bannerState.value == Showing
      }
    }

    div {
      when (props.bannerType) {
        BannerType.Info -> i(classes = "fas fa-info-circle") {}
        BannerType.Warning -> i(classes = "fas fa-exclamation-triangle") {}
        BannerType.Danger -> i(classes = "fas fa-exclamation-circle") {}
        BannerType.Success -> i(classes = "fas fa-check-circle") {}
      }

      strong(classes = "mx-1 alert-heading") {
        +props.title
      }
    }

    props.block(this)

    button(classes = "btn-close") {
      attrs {
        onClickFunction = {
          props.bannerState.setter(Hidden)
        }
      }
    }
  }
}

enum class BannerType {
  Info,
  Warning,
  Danger,
  Success,
}

enum class BannerOrientation {
  Top,
  Bottom,
}

enum class BannerState {
  Hidden,
  Showing,
}


external interface NotificationBannerProps : Props {
  var title: String
  var bannerType: BannerType
  var bannerOrientation: BannerOrientation
  var bannerState: StateInstance<BannerState>
  var block: (RDOMBuilder<DIV>) -> Unit
}
