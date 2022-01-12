package it.neckar.react.common

import it.neckar.react.common.BannerPlacement.*
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
  bannerPlacement: BannerPlacement,
  onClose: (() -> Unit)? = null,
  block: RDOMBuilder<DIV>.() -> Unit,
): Unit = child(notificationBanner) {
  attrs {
    this.title = title
    this.bannerType = bannerType
    this.bannerPlacement = bannerPlacement
    this.onClose = onClose
    this.block = block
  }
}

val notificationBanner: FC<NotificationBannerProps> = fc("notificationBanner") { props ->
  div(classes = "alert alert-dismissible") {
    attrs {
      when (props.bannerType) {
        BannerType.Info -> addClass("alert-info")
        BannerType.Warning -> addClass("alert-warning")
        BannerType.Danger -> addClass("alert-danger")
        BannerType.Success -> addClass("alert-success")
      }

      when (props.bannerPlacement) {
        Top -> addClass("fixed-top")
        Bottom -> addClass("fixed-bottom")
      }
    }

    div("mb-2") {
      when (props.bannerType) {
        BannerType.Info -> i(classes = "fas fa-info-circle") {}
        BannerType.Warning -> i(classes = "fas fa-exclamation-triangle") {}
        BannerType.Danger -> i(classes = "fas fa-exclamation-circle") {}
        BannerType.Success -> i(classes = "fas fa-check-circle") {}
      }

      strong(classes = "mx-2 alert-heading") {
        +props.title
      }
    }

    props.onClose?.let { onClose ->
      button(classes = "btn-close") {
        attrs {
          onClickFunction = { onClose() }
        }
      }
    }

    props.block(this)
  }
}

enum class BannerType {
  Info,
  Warning,
  Danger,
  Success,
}

enum class BannerPlacement {
  Top,
  Bottom,
}


external interface NotificationBannerProps : Props {
  var title: String
  var bannerType: BannerType
  var bannerPlacement: BannerPlacement
  var onClose: (() -> Unit)?
  var block: (RDOMBuilder<DIV>) -> Unit
}
