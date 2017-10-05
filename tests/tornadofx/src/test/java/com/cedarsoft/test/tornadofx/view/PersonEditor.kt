package com.cedarsoft.test.tornadofx.view

import javafx.beans.binding.BooleanExpression
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Orientation
import tornadofx.*

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class CombinedView : View("Persons") {
  val personListView: PersonListView by inject()
  val personEditor: PersonEditor by inject()

  override val root = splitpane(Orientation.HORIZONTAL) {
    add(personListView)
    add(personEditor)
  }
}


class PersonListView : View("Person List") {
  private val model: PersonModel by inject()
  private val personController: PersonController by inject()

  override val savable: BooleanExpression = SimpleBooleanProperty(false)
  override val refreshable: BooleanExpression = SimpleBooleanProperty()
  override val deletable: BooleanExpression = SimpleBooleanProperty()
  override val creatable: BooleanExpression = SimpleBooleanProperty()

  override val root = tableview(personController.persons) {
    column("Title", Person::titleProperty).apply {
      prefWidth = 100.0
    }
    column("Name", Person::nameProperty).apply {
      prefWidth = 150.0
    }

    // Update the person inside the view model on selection change
    bindSelected(model)
  }
}

class PersonEditor : View("Person Editor") {
  val model: PersonModel by inject()

  override val root = form {
    fieldset("Edit person") {
      field("Name") {
        textfield(model.name)
          .required()
      }
      field("Title") {
        textfield(model.title)
          .validator {
            if (it.isNullOrBlank()) {
              return@validator error("Is required")
            }

            if (it.equals("Forbidden")) {
              return@validator error("Is Forbidden")
            }

            return@validator null;
          }
      }
      button("Save") {
        enableWhen(model.dirty)
        action {
          println("model.backingValue(model.name) = ${model.backingValue(model.name)}")
          save()
          println("model.backingValue(model.name) = ${model.backingValue(model.name)}")
        }
      }
      button("Reset") {
        enableWhen(model.dirty)
        action {
          model.rollback()
        }
      }
    }
  }


  private fun save() {
    // Flush changes from the text fields into the model
    model.commit()

    // The edited person is contained in the model
    val person = model.item

    // A real application would persist the person here
    println("Saving ${person?.name} / ${person?.title}")
  }
}

class PersonModel() : ItemViewModel<Person>() {
  val name = bind(Person::nameProperty)
  val title = bind(Person::titleProperty)

  override fun onCommit(commits: List<Commit>) {
    super.onCommit(commits)

    commits.forEach {
      println("Commit: ${it.oldValue} --> ${it.newValue} (${it.changed})")
    }
  }
}

//class PersonModel(var person: Person) : ViewModel() {
//  val name = bind { person.nameProperty }
//  val title = bind { person.titleProperty }
//}
//
