package it.neckar.open.tornadofx

import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.Parent
import net.miginfocom.layout.AC
import net.miginfocom.layout.CC
import net.miginfocom.layout.ConstraintParser
import net.miginfocom.layout.LC
import org.tbee.javafx.scene.layout.MigPane
import tornadofx.*

/**
 * Contains helper classes for mig pane
 *
 */

/**
 * Creates a new mig pane
 */
fun EventTarget.migPane(op: MigPane.() -> Unit = {}): MigPane {
  val migPane = MigPane()
  return migPane.attachTo(this, op)
}

/**
 * Modifies the layout constraints
 */
fun MigPane.layoutConstraints(op: LC.() -> Unit = {}) {
  op(this.layoutConstraints)

  //Force recalculation of debug state. The debug state is only checked when the setter is called
  layoutConstraints = layoutConstraints
}

/**
 * Parses the layout constraints from the given string
 */
fun MigPane.layoutConstraints(constraints: String) {
  layoutConstraints = ConstraintParser.parseLayoutConstraint(ConstraintParser.prepare(constraints))

  //Force recalculation of debug state. The debug state is only checked when the setter is called
  layoutConstraints = layoutConstraints
}

/**
 * Modifies the columns constraints
 */
fun MigPane.columnConstraints(op: AC.() -> Unit = {}) {
  op(this.columnConstraints)
}

fun MigPane.columnConstraints(constraints: String) {
  columnConstraints = ConstraintParser.parseColumnConstraints(ConstraintParser.prepare(constraints))
}

fun MigPane.rowConstraints(constraints: String) {
  rowConstraints = ConstraintParser.parseRowConstraints(ConstraintParser.prepare(constraints))
}

/**
 * Modifies the row constraints
 */
fun MigPane.rowConstraints(op: AC.() -> Unit = {}) {
  op(this.rowConstraints)
}

fun <T : Node> T.componentConstraints(constraints: String): T {
  val parent: Parent = this.parent ?: throw IllegalStateException("No parent set")

  if (parent !is MigPane) {
    throw IllegalStateException("Invalid parent type <${parent.javaClass.name}>. Expected <${MigPane::class.java.name}>")
  }

  parent.setComponentConstraints(this, constraints)
  return this
}

/**
 * Helper method to modify the constraints
 */
inline fun <T : Node> T.componentConstraints(op: (CC.() -> Unit)): T {
  val parent: Parent = this.parent ?: throw IllegalStateException("No parent set")

  if (parent !is MigPane) {
    throw IllegalStateException("Invalid parent type <${parent.javaClass.name}>. Expected <${MigPane::class.java.name}>")
  }

  //Ensure there are components. If there are none, create them
  if (parent.getComponentConstraints(this) == null) {
    parent.setComponentConstraints(this, "")
  }

  val componentConstraints = parent.getComponentConstraints(this) ?: throw IllegalStateException("No constraints found")
  componentConstraints.op()
  return this
}
