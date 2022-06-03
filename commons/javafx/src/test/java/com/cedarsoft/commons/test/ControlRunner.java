/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
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
package com.cedarsoft.commons.test;

import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.control.GlyphCheckBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.dialog.CommandLinksDialog;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.LoginDialog;
import org.tbee.javafx.scene.layout.MigPane;

/**
 */
public class ControlRunner extends Application {
  public static void main(String[] args) {
    //System.setProperty("prism.lcdtext", "true");
    //System.setProperty("prism.text", "t2k");

    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    MigPane root = new MigPane("insets 0 10 10 10", "", "");

    MenuBar menuBar = new MenuBar(new Menu("File"), new Menu("Edit"));
    root.add(menuBar, "wrap, span, growx");

    ToolBar toolBar = new ToolBar(new Button(null, GlyphsDude.createIcon(MaterialDesignIcon.CONTENT_SAVE_ALL)), new Button(null, GlyphsDude.createIcon(MaterialDesignIcon.TABLE_EDIT)));
    root.add(toolBar, "wrap, span, growx");


    CheckBox checkbox = new CheckBox("Hello Checkbox");
    checkbox.setAllowIndeterminate(true);

    Button button = new Button("A Button");
    button.onActionProperty().setValue(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ContextMenu contextMenu = new ContextMenu(new MenuItem("1"), new MenuItem("2"));
        contextMenu.show(button, Side.RIGHT, 10, 10);
      }
    });

    Accordion accordion = new Accordion(
      new TitledPane("Hello World", button),
      new TitledPane("Hello World 2", checkbox)
    );

    root.add(accordion);
    root.add(new ChoiceBox<>(FXCollections.observableArrayList("A", "B", "C", "D")));

    root.add(new ColorPicker());

    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("A", "B", "C", "D"));
    comboBox.setEditable(true);
    root.add(comboBox);

    root.add(new Hyperlink("heise.de"));
    root.add(new Label("heise.de"), "wrap");

    ListView<String> listView = new ListView<>(FXCollections.observableArrayList("A", "B", "C", "D"));
    listView.setEditable(true);
    listView.setOnEditStart(new EventHandler<ListView.EditEvent<String>>() {
      @Override
      public void handle(ListView.EditEvent<String> event) {
        System.out.println("Start Editing");
      }
    });
    root.add(listView);

    Label label = new Label("TEST");
    label.setStyle("-fx-background-color:WHITE;-fx-padding:20");
    root.add(new JFXRippler(label));


    GlyphCheckBox glyphCheckBox = new GlyphCheckBox(
      new FontAwesomeIconView(FontAwesomeIcon.ALIGN_RIGHT),
      new FontAwesomeIconView(FontAwesomeIcon.ALIGN_LEFT),
      "Alignment"
    );
    root.add(glyphCheckBox);

    root.add(new FontAwesomeIconView(FontAwesomeIcon.BUILDING_ALT));
    root.add(GlyphsDude.createIcon(MaterialDesignIcon.ACCOUNT_LOCATION, "30"));
    root.add(GlyphsDude.createIcon(MaterialDesignIcon.ACCOUNT_MINUS, "30"));


    TreeItem<String> stringTreeItem = new TreeItem<>("1");
    stringTreeItem.getChildren().add(new TreeItem<>("2"));
    stringTreeItem.getChildren().add(new TreeItem<>("3"));
    stringTreeItem.getChildren().add(new TreeItem<>("4"));

    BreadCrumbBar<String> breadCrumbBar = new BreadCrumbBar<>(stringTreeItem.getChildren().get(0));
    breadCrumbBar.setAutoNavigationEnabled(true);

    root.add(breadCrumbBar);

    Button notifactionButton = new Button("Show notification");
    notifactionButton.onActionProperty().setValue(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Notifications.create()
          //.darkStyle()
          .title("The Title")
          .text("The text")
          .graphic(new ImageView(Notifications.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-information.png").toExternalForm()))
          .show()
        ;

        PopOver popOver = new PopOver(new Label("This is the content"));
        popOver.animatedProperty().setValue(true);
        popOver.show(notifactionButton);


        ExceptionDialog exceptionDialog = new ExceptionDialog(new IllegalArgumentException());
        exceptionDialog.show();

        Callback<Pair<String, String>, Void> authenticator = new Callback<Pair<String, String>, Void>() {
          @Override
          public Void call(Pair<String, String> param) {
            return null;
          }
        };
        LoginDialog loginDialog = new LoginDialog(new Pair<>("Hey", "you"), authenticator);
        loginDialog.show();


        CommandLinksDialog commandLinksDialog = new CommandLinksDialog(
          new CommandLinksDialog.CommandLinksButtonType("Hello", "This is the very long text", true),
          new CommandLinksDialog.CommandLinksButtonType("Hello2", "This is the very long text 2", false)
        );
        commandLinksDialog.show();
      }
    });
    root.add(notifactionButton);

    Button dialogButton = new Button("Dialog");
    dialogButton.onActionProperty().setValue(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION, "Hello World");
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.initOwner(primaryStage);
        dlg.show();
      }
    });
    root.add(dialogButton);


    Pagination pagination = new Pagination(70);
    pagination.setPageFactory(new Callback<Integer, Node>() {
      @Override
      public Node call(Integer param) {
        return new Label(String.valueOf(param));
      }
    });

    root.add(pagination);


    PasswordField passwordField = new PasswordField();
    passwordField.setText("asdf");
    root.add(passwordField);

    ProgressBar progressBar = new ProgressBar(0.5);
    root.add(progressBar);

    Slider slider = new Slider(0, 1, 0.1);
    root.add(slider, "wrap");

    progressBar.progressProperty().bind(slider.valueProperty());

    root.add(new Separator(Orientation.HORIZONTAL), "span, grow");


    SplitPane splitPane = new SplitPane(new TextArea("Left"), new TextArea("Right"));
    root.add(splitPane, "span, wrap, grow");

    root.add(new TextField("Da Input Text"));


    root.add(new TitledPane("Da Title", new Button("asdf")));
    ToggleButton toggleButton = new ToggleButton("Do it!", GlyphsDude.createIcon(MaterialDesignIcon.WEATHER_SUNSET_UP, "30"));
    root.add(toggleButton);

    toggleButton.setTooltip(new Tooltip("Hello Auto"));
    toggleButton.setContentDisplay(ContentDisplay.BOTTOM);


    TreeItem<String> rootItem = new TreeItem<>("Da Root");
    for (int i = 0; i < 100; i++) {
      rootItem.getChildren().add(new TreeItem<>("Child" + i));
    }
    TreeView<String> treeView = new TreeView<>(rootItem);

    root.add(treeView);


    ObservableList<Customer> customers = FXCollections.observableArrayList();
    for (int i = 0; i < 1000; i++) {
      Customer customer = new Customer();
      customer.setName("Customer " + i);
      customer.addressesProperty().add(new Customer.Address());
      customers.add(customer);
    }

    FilteredList<Customer> filteredCustomers = new FilteredList<Customer>(customers);
    SortedList<Customer> sortedCustomers = new SortedList<>(filteredCustomers);

    TableView<Customer> table = new TableView<>(sortedCustomers);
    TableColumn<Customer, String> nameColumn = new TableColumn<>("Column 1");
    table.getColumns().add(nameColumn);
    TableColumn<Customer, Integer> addressesColumn = new TableColumn<>("Column 2");
    addressesColumn.setSortable(false);
    table.getColumns().add(addressesColumn);
    root.add(table, "wrap, span");

    TextField filterField = new TextField();
    root.add(filterField);

    //Bind the comparator property
    sortedCustomers.comparatorProperty().bind(table.comparatorProperty());


    filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredCustomers.setPredicate(customer -> {
      if (newValue == null || newValue.isEmpty()) {
        return true;
      }

      if (customer.getName().contains(newValue)) {
        return true;
      }

      for (Customer.Address address : customer.getAddresses()) {
        if (address.getCity().contains(newValue)) {
          return true;
        }
        if (address.getStreet().contains(newValue)) {
          return true;
        }
        if (String.valueOf(address.getNumber()).contains(newValue)) {
          return true;
        }
      }

      return false;
    }));

    nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> param) {
        return param.getValue().nameProperty();
      }
    });
    addressesColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, Integer>, ObservableValue<Integer>>() {
      @Override
      public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Customer, Integer> param) {
        return Bindings.size(param.getValue().addressesProperty()).asObject();
      }
    });


    Label longLabel = new Label("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    longLabel.setWrapText(true);
    root.add(longLabel, "span");


    Pane region = new Pane();
    region.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));

    Circle circle = new Circle(100);
    circle.setFill(Color.ORANGE);
    region.getChildren().add(circle);

    root.add(region);

    region.scaleXProperty().bind(
      slider.valueProperty()
    );
    region.scaleYProperty().bind(
      slider.valueProperty()
    );


    Scene scene = new Scene(root, 1920, 1080);

    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
