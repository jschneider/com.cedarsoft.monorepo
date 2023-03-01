/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package it.neckar.open.test

import it.neckar.open.resources.getResourceSafe
import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.control.GlyphCheckBox
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.collections.transformation.SortedList
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.control.Accordion
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.ComboBox
import javafx.scene.control.ContentDisplay
import javafx.scene.control.ContextMenu
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.Pagination
import javafx.scene.control.PasswordField
import javafx.scene.control.ProgressBar
import javafx.scene.control.Separator
import javafx.scene.control.Slider
import javafx.scene.control.SplitPane
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.util.Callback
import javafx.util.Pair
import org.controlsfx.control.BreadCrumbBar
import org.controlsfx.control.Notifications
import org.controlsfx.control.PopOver
import org.controlsfx.dialog.CommandLinksDialog
import org.controlsfx.dialog.ExceptionDialog
import org.controlsfx.dialog.LoginDialog
import org.tbee.javafx.scene.layout.MigPane

/**
 */
class ControlRunner : Application() {
  override fun start(primaryStage: Stage) {

    val root = MigPane("insets 0 10 10 10", "", "")
    val menuBar = MenuBar(Menu("File"), Menu("Edit"))
    root.add(menuBar, "wrap, span, growx")
    val toolBar = ToolBar(Button(null, GlyphsDude.createIcon(MaterialDesignIcon.CONTENT_SAVE_ALL)), Button(null, GlyphsDude.createIcon(MaterialDesignIcon.TABLE_EDIT)))
    root.add(toolBar, "wrap, span, growx")
    val checkbox = CheckBox("Hello Checkbox")
    checkbox.isAllowIndeterminate = true

    val button = Button("A Button")
    button.onActionProperty().setValue(EventHandler {
      val contextMenu = ContextMenu(MenuItem("1"), MenuItem("2"))
      contextMenu.show(button, Side.RIGHT, 10.0, 10.0)
    })

    val accordion = Accordion(
      TitledPane("Hello World", button),
      TitledPane("Hello World 2", checkbox)
    )
    root.add(accordion)
    root.add(ChoiceBox(FXCollections.observableArrayList("A", "B", "C", "D")))
    root.add(ColorPicker())

    val comboBox = ComboBox(FXCollections.observableArrayList("A", "B", "C", "D"))
    comboBox.isEditable = true
    root.add(comboBox)
    root.add(Hyperlink("heise.de"))
    root.add(Label("heise.de"), "wrap")
    val listView = ListView(FXCollections.observableArrayList("A", "B", "C", "D"))
    listView.isEditable = true
    listView.setOnEditStart { println("Start Editing") }
    root.add(listView)

    val label = Label("TEST")
    label.style = "-fx-background-color:WHITE;-fx-padding:20"
    root.add(JFXRippler(label))
    val glyphCheckBox = GlyphCheckBox(
      FontAwesomeIconView(FontAwesomeIcon.ALIGN_RIGHT),
      FontAwesomeIconView(FontAwesomeIcon.ALIGN_LEFT),
      "Alignment"
    )
    root.add(glyphCheckBox)
    root.add(FontAwesomeIconView(FontAwesomeIcon.BUILDING_ALT))
    root.add(GlyphsDude.createIcon(MaterialDesignIcon.ACCOUNT_LOCATION, "30"))
    root.add(GlyphsDude.createIcon(MaterialDesignIcon.ACCOUNT_MINUS, "30"))

    val stringTreeItem = TreeItem("1")
    stringTreeItem.children.add(TreeItem("2"))
    stringTreeItem.children.add(TreeItem("3"))
    stringTreeItem.children.add(TreeItem("4"))
    val breadCrumbBar = BreadCrumbBar(stringTreeItem.children[0])
    breadCrumbBar.isAutoNavigationEnabled = true
    root.add(breadCrumbBar)

    val notifactionButton = Button("Show notification")
    notifactionButton.onActionProperty().setValue(EventHandler {
      Notifications.create() //.darkStyle()
        .title("The Title")
        .text("The text")
        .graphic(ImageView(Notifications::class.java.getResourceSafe("/com/sun/javafx/scene/control/skin/caspian/dialog-information.png").toExternalForm()))
        .show()
      val popOver = PopOver(Label("This is the content"))
      popOver.animatedProperty().value = true
      popOver.show(notifactionButton)

      val exceptionDialog = ExceptionDialog(IllegalArgumentException())
      exceptionDialog.show()

      val authenticator: Callback<Pair<String, String>, Void> = Callback<Pair<String, String>, Void> { null }
      val loginDialog = LoginDialog(Pair("Hey", "you"), authenticator)
      loginDialog.show()

      val commandLinksDialog = CommandLinksDialog(
        CommandLinksDialog.CommandLinksButtonType("Hello", "This is the very long text", true),
        CommandLinksDialog.CommandLinksButtonType("Hello2", "This is the very long text 2", false)
      )
      commandLinksDialog.show()
    })
    root.add(notifactionButton)
    val dialogButton = Button("Dialog")
    dialogButton.onActionProperty().setValue(EventHandler {
      val dlg = Alert(Alert.AlertType.INFORMATION, "Hello World")
      dlg.initModality(Modality.APPLICATION_MODAL)
      dlg.initOwner(primaryStage)
      dlg.show()
    })
    root.add(dialogButton)
    val pagination = Pagination(70)
    pagination.pageFactory = Callback { param -> Label(param.toString()) }
    root.add(pagination)
    val passwordField = PasswordField()
    passwordField.text = "asdf"
    root.add(passwordField)
    val progressBar = ProgressBar(0.5)
    root.add(progressBar)
    val slider = Slider(0.0, 1.0, 0.1)
    root.add(slider, "wrap")
    progressBar.progressProperty().bind(slider.valueProperty())
    root.add(Separator(Orientation.HORIZONTAL), "span, grow")
    val splitPane = SplitPane(TextArea("Left"), TextArea("Right"))
    root.add(splitPane, "span, wrap, grow")
    root.add(TextField("Da Input Text"))
    root.add(TitledPane("Da Title", Button("asdf")))
    val toggleButton = ToggleButton("Do it!", GlyphsDude.createIcon(MaterialDesignIcon.WEATHER_SUNSET_UP, "30"))
    root.add(toggleButton)
    toggleButton.tooltip = Tooltip("Hello Auto")
    toggleButton.contentDisplay = ContentDisplay.BOTTOM
    val rootItem = TreeItem("Da Root")
    for (i in 0..99) {
      rootItem.children.add(TreeItem("Child$i"))
    }
    val treeView = TreeView(rootItem)
    root.add(treeView)
    val customers = FXCollections.observableArrayList<Customer>()
    for (i in 0..999) {
      val customer = Customer()
      customer.name = "Customer $i"
      customer.addresses.add(Customer.Address())
      customers.add(customer)
    }
    val filteredCustomers = FilteredList(customers)
    val sortedCustomers = SortedList(filteredCustomers)
    val table = TableView(sortedCustomers)
    val nameColumn = TableColumn<Customer, String>("Column 1")
    table.columns.add(nameColumn)
    val addressesColumn = TableColumn<Customer, Int>("Column 2")
    addressesColumn.isSortable = false
    table.columns.add(addressesColumn)
    root.add(table, "wrap, span")
    val filterField = TextField()
    root.add(filterField)

    //Bind the comparator property
    sortedCustomers.comparatorProperty().bind(table.comparatorProperty())
    filterField.textProperty().addListener { observable: ObservableValue<out String?>?, oldValue: String?, newValue: String? ->
      filteredCustomers.setPredicate { customer: Customer ->
        if (newValue == null || newValue.isEmpty()) {
          return@setPredicate true
        }
        if (customer.name.contains(newValue)) {
          return@setPredicate true
        }
        for (address in customer.addresses) {
          if (address.city.contains(newValue)) {
            return@setPredicate true
          }
          if (address.street.contains(newValue)) {
            return@setPredicate true
          }
          if (address.number.toString().contains(newValue)) {
            return@setPredicate true
          }
        }
        false
      }
    }

    nameColumn.setCellValueFactory {
      it.value.nameProperty
    }

    addressesColumn.setCellValueFactory { param -> Bindings.size(param.value.addresses).asObject() }

    val longLabel =
      Label("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.")
    longLabel.isWrapText = true
    root.add(longLabel, "span")
    val region = Pane()
    region.background = Background(BackgroundFill(Color.BLUE, null, null))
    val circle = Circle(100.0)
    circle.fill = Color.ORANGE
    region.children.add(circle)
    root.add(region)
    region.scaleXProperty().bind(
      slider.valueProperty()
    )
    region.scaleYProperty().bind(
      slider.valueProperty()
    )
    val scene = Scene(root, 1920.0, 1080.0)
    scene.stylesheets.add(javaClass.getResourceSafe("application.css").toExternalForm())
    primaryStage.scene = scene
    primaryStage.show()
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      //System.setProperty("prism.lcdtext", "true");
      //System.setProperty("prism.text", "t2k");
      launch(*args)
    }
  }
}
