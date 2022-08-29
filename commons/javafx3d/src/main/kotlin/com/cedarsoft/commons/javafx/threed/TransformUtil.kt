package com.cedarsoft.commons.javafx.threed

import com.google.common.collect.ImmutableList
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.scene.Node
import javafx.scene.transform.NonInvertibleTransformException
import javafx.scene.transform.Rotate
import javafx.scene.transform.Transform
import javafx.scene.transform.Translate
import org.slf4j.LoggerFactory

/**
 * A utility class around [Transform]s.
 */
object TransformUtil {
  private val LOG = LoggerFactory.getLogger(TransformUtil::class.java.name)

  @JvmStatic
  fun installInverseRotate(referenceNode: Node, vararg nodes: Node) {
    installInverseRotate(referenceNode, ImmutableList.copyOf(nodes))
  }

  /**
   * Whenever the reference node is rotated (via a [Rotate] transformation) all other nodes a rotated with the opposite angle.
   */
  @JvmStatic
  fun installInverseRotate(referenceNode: Node, nodes: Collection<Node>) {
    // Remove all old rotate transformations.
    nodes.forEach {
      removeAllRotations(it)
    }

    // Add a ChangeListener for all current rotations set on the reference node.
    val rotateListener = ChangeListener { observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number? -> invertRotate(referenceNode, nodes) }
    referenceNode.transforms.stream().filter { transform: Transform? -> transform is Rotate }.forEach { rotate: Transform -> (rotate as Rotate).angleProperty().addListener(rotateListener) }

    // Add a ChangeListener for all new rotations set on the reference node and
    // delete the old ChangeListener for all rotations removed from the reference node.
    referenceNode.transforms.addListener { change: ListChangeListener.Change<out Transform> ->
      while (change.next()) {
        change.removed.stream().filter { transform: Transform? -> transform is Rotate }.forEach { rotate: Transform -> (rotate as Rotate).angleProperty().removeListener(rotateListener) }
        change.addedSubList.stream().filter { transform: Transform? -> transform is Rotate }.forEach { rotate: Transform -> (rotate as Rotate).angleProperty().addListener(rotateListener) }
      }
      invertRotate(referenceNode, nodes)
    }
    invertRotate(referenceNode, nodes)
  }

  /**
   * Inverts all transforms of type [Rotate] from the reference node and applies them to each node of the list.
   */
  private fun invertRotate(referenceNode: Node, nodes: Collection<Node>) {
    val transforms = referenceNode.transforms
    if (transforms.isEmpty()) {
      return
    }
    var combinedRotate: Transform = Translate(0.0, 0.0, 0.0)
    for (transform in transforms) {
      if (transform !is Rotate) {
        continue
      }
      combinedRotate = combinedRotate.createConcatenation(transform)
    }
    try {
      val inverseTransform = combinedRotate.createInverse()
      nodes.forEach { node: Node -> node.transforms.setAll(inverseTransform) }
    } catch (e: NonInvertibleTransformException) {
      LOG.error("failed to invert rotation", e)
    }
  }

  /**
   * Remove all instances of [Rotate] from the list of transforms of the given node.
   */
  @JvmStatic
  fun removeAllRotations(node: Node) {
    node.transforms.removeIf { transform: Transform? -> transform is Rotate }
  }

  /**
   * Binds all translations and rotations from a target node to a source node.
   */
  @JvmStatic
  fun bindAll(source: Node, target: Node) {
    target.translateXProperty().bind(source.translateXProperty())
    target.translateYProperty().bind(source.translateYProperty())
    target.translateZProperty().bind(source.translateZProperty())
    target.scaleXProperty().bind(source.scaleXProperty())
    target.scaleYProperty().bind(source.scaleYProperty())
    target.scaleZProperty().bind(source.scaleZProperty())
    target.rotateProperty().bind(source.rotateProperty())
    target.rotationAxisProperty().bind(source.rotationAxisProperty())
    source.transforms.addListener { change: ListChangeListener.Change<out Transform?>? -> target.transforms.setAll(source.transforms) }
    target.transforms.setAll(source.transforms)
  }
}
