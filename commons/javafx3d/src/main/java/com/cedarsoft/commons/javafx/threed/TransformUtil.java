package com.cedarsoft.commons.javafx.threed;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

/**
 * A utility class around {@link Transform}s.
 */
public class TransformUtil {
  @Nonnull 
  private static final Logger LOG = LoggerFactory.getLogger(TransformUtil.class.getName());

  private TransformUtil() {
    // utility class
  }

  public static void installInverseRotate(@Nonnull Node referenceNode, @Nonnull Node... nodes) {
    installInverseRotate(referenceNode, ImmutableList.copyOf(nodes));
  }

  /**
   * Whenever the reference node is rotated (via a {@link Rotate} transformation) all other nodes a rotated with the opposite angle.
   */
  public static void installInverseRotate(@Nonnull Node referenceNode, @Nonnull Collection<? extends Node> nodes) {
    // Remove all old rotate transformations.
    nodes.forEach(TransformUtil::removeAllRotations);

    // Add a ChangeListener for all current rotations set on the reference node.
    ChangeListener<Number> rotateListener = (observable, oldValue, newValue) -> invertRotate(referenceNode, nodes);
    referenceNode.getTransforms().stream()
      .filter(transform -> transform instanceof Rotate)
      .forEach(rotate -> ((Rotate) rotate).angleProperty().addListener(rotateListener));

    // Add a ChangeListener for all new rotations set on the reference node and
    // delete the old ChangeListener for all rotations removed from the reference node.
    referenceNode.getTransforms().addListener((ListChangeListener<Transform>) change -> {
      while (change.next()) {
        change.getRemoved().stream().filter(transform -> transform instanceof Rotate).forEach(rotate -> {
          ((Rotate) rotate).angleProperty().removeListener(rotateListener);
        });
        change.getAddedSubList().stream().filter(transform -> transform instanceof Rotate).forEach(rotate -> {
          ((Rotate) rotate).angleProperty().addListener(rotateListener);
        });
      }
      invertRotate(referenceNode, nodes);
    });

    invertRotate(referenceNode, nodes);
  }

  /**
   * Inverts all transforms of type {@link Rotate} from the reference node and applies them to each node of the list.
   */
  private static void invertRotate(@Nonnull Node referenceNode, @Nonnull Collection<? extends Node> nodes) {
    ObservableList<Transform> transforms = referenceNode.getTransforms();
    if (transforms.isEmpty()) {
      return;
    }
    Transform combinedRotate = new Translate(0, 0, 0);
    for (Transform transform : transforms) {
      if (!(transform instanceof Rotate)) {
        continue;
      }
      combinedRotate = combinedRotate.createConcatenation(transform);
    }
    try {
      Transform inverseTransform = combinedRotate.createInverse();
      nodes.forEach(node -> node.getTransforms().setAll(inverseTransform));
    } catch (NonInvertibleTransformException e) {
      LOG.error("failed to invert rotation", e);
    }
  }

  /**
   * Remove all instances of {@link Rotate} from the list of transforms of the given node.
   */
  public static void removeAllRotations(@Nonnull Node node) {
    node.getTransforms().removeIf(transform -> transform instanceof Rotate);
  }

  /**
   * Binds all translations and rotations from a target node to a source node.
   */
  public static void bindAll(@Nonnull Node source, @Nonnull Node target) {
    target.translateXProperty().bind(source.translateXProperty());
    target.translateYProperty().bind(source.translateYProperty());
    target.translateZProperty().bind(source.translateZProperty());

    target.scaleXProperty().bind(source.scaleXProperty());
    target.scaleYProperty().bind(source.scaleYProperty());
    target.scaleZProperty().bind(source.scaleZProperty());

    target.rotateProperty().bind(source.rotateProperty());
    target.rotationAxisProperty().bind(source.rotationAxisProperty());

    source.getTransforms().addListener((ListChangeListener<Transform>) change -> target.getTransforms().setAll(source.getTransforms()));
    target.getTransforms().setAll(source.getTransforms());
  }
}
