package com.cedarsoft.test.tornadofx.view

import javafx.scene.Parent
import tornadofx.*

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class MyApp2 : App(Workspace::class) {
  override fun onBeforeShow(view: UIComponent) {
    super.onBeforeShow(view)

    workspace.dock<DaTabbed>()
  }
}

class DaTabbed : View("Persons Editor") {
  override val root: Parent = tabpane {
    tab(PersonListView::class)
    tab(PersonEditor::class)
    connectWorkspaceActions()
  }
}
