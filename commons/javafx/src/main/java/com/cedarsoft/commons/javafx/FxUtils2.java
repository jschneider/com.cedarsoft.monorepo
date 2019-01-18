package com.cedarsoft.commons.javafx;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.unit.other.px;
import com.google.common.collect.ImmutableList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

/**
 * Helper class for node related calculations
 */
public class FxUtils2 {
  /**
   * The amount of pixels that is added to ensure the node is not exactly at the edge
   */
  static final int SCROLL_TO_VISIBLE_MARGIN = 10;

  private FxUtils2() {
  }

  /**
   * Scrolls the given value to visible using an animation
   */
  static void scrollToVisible(@Nonnull ScrollPane scrollPane, double targetVValue, boolean animated) {
    if (!animated) {
      scrollPane.vvalueProperty().setValue(targetVValue);
      return;
    }

    final Timeline l_timeline = new Timeline();
    l_timeline.getKeyFrames().add(
      new KeyFrame(Duration.millis(200),
                   new KeyValue(scrollPane.vvalueProperty(), targetVValue, Interpolator.EASE_BOTH))
    );
    l_timeline.play();
  }

  /**
   * Scrolls the given node to visible within the first scroll pane that is found as ancestor
   */
  public static void scrollToVisible(@Nonnull Node node) {
    ScrollPane l_scrollPane = findParent(ScrollPane.class, node).orElseThrow(() -> new IllegalStateException("No parent scroll pane found"));
    scrollToVisible(l_scrollPane, node);
  }

  /**
   * Scrolls scroll pane to make the given node visible
   */
  public static void scrollToVisible(@Nonnull ScrollPane scrollPane, @Nonnull Node node) {
    Point2D l_positionInViewPort = localToAncestor(node, scrollPane.getContent(), 0, 0);
    @px double l_nodeHeight = node.getLayoutBounds().getHeight();

    scrollToVisible(scrollPane, l_positionInViewPort.getY(), l_nodeHeight);
  }

  /**
   * Scrolls the bar so that the target y is in the center of the visible area
   */
  public static void scrollToCenter(@Nonnull ScrollPane scrollPane, @px double targetY) {
    scrollToCenter(scrollPane, targetY, true);
  }

  public static void scrollToCenter(@Nonnull ScrollPane scrollPane, @px double targetY, boolean animated) {
    @px double l_height = scrollPane.getViewportBounds().getHeight();
    scrollToVisible(scrollPane, targetY - l_height / 2.0, l_height, 0, animated);
  }

  /**
   * Scrolls an area to visible
   *
   * @param scrollPane   the pane that is moved
   * @param targetY      the target y that should be visible
   * @param targetHeight the target height that should be visible
   */
  public static void scrollToVisible(@Nonnull ScrollPane scrollPane, @px double targetY, @px double targetHeight) {
    scrollToVisible(scrollPane, targetY, targetHeight, SCROLL_TO_VISIBLE_MARGIN, true);
  }

  public static void scrollToVisible(@Nonnull ScrollPane scrollPane, @px double targetY, @px double targetHeight, @px double scrollMargin, boolean animated) {
    //The height of the view port
    @px double l_viewportHeight = scrollPane.getViewportBounds().getHeight();
    //The height of the content
    @px double l_contentHeight = scrollPane.getContent().getLayoutBounds().getHeight();
    //The height that is always hidden
    @px double l_hiddenHeight = l_contentHeight - l_viewportHeight;

    //The current min/max y values that are visible
    @px double l_currentlyVisibleTop = scrollPane.getVvalue() * l_hiddenHeight;
    @px double l_currentlyVisibleBottom = l_currentlyVisibleTop + l_viewportHeight;


    //The bottom y for the target area
    @px double l_targetYBottom = targetY + targetHeight;


    //Check for large target heights
    if (targetHeight > l_viewportHeight) {
      if (targetY < l_currentlyVisibleTop && l_targetYBottom > l_currentlyVisibleBottom) {
        //Do nothing, since some parts are visible
        return;
      }
    }


    if (targetY < l_currentlyVisibleTop) {
      //target y is above the currently visible area, we have to scroll
      double l_newVValue = minMax(0.0, 1.0, 1.0 / l_hiddenHeight * (targetY - scrollMargin));
      scrollToVisible(scrollPane, l_newVValue, animated);
      return;
    }


    if (l_targetYBottom > l_currentlyVisibleBottom) {
      //The target height is *not* larger than the viewport height, Just scroll
      double l_newVValue = minMax(0, 1.0, 1.0 / l_hiddenHeight * (l_targetYBottom - l_viewportHeight + scrollMargin));
      scrollToVisible(scrollPane, l_newVValue, animated);

      //noinspection UnnecessaryReturnStatement
      return;
    }

    //Currently visible, do nothing
  }

  /**
   * Ensures the given values is within the given min/max values
   */
  private static double minMax(double min, double max, double current) {
    return Math.max(min, Math.min(max, current));
  }

  /**
   * Returns the first parent of the given type
   */
  @Nonnull
  public static <T extends Node> Optional<T> findParent(@Nonnull Class<T> type, @Nonnull Node node) {
    @Nullable Node l_currentNode = node;

    while (l_currentNode != null) {
      if (type.isAssignableFrom(l_currentNode.getClass())) {
        return Optional.of(type.cast(l_currentNode));
      }
      l_currentNode = l_currentNode.getParent();
    }

    return Optional.empty();
  }

  /**
   * Calculates the location relative to the ancestor
   */
  public static Point2D localToAncestor(@Nonnull Node node, @Nonnull Node ancestor, double x, double y) {
    Point2D point2D = new Point2D(x, y);

    Node l_currentNode = node;
    while (l_currentNode != null) {
      if (l_currentNode == ancestor) {
        return point2D;
      }

      point2D = l_currentNode.localToParent(point2D);

      l_currentNode = l_currentNode.getParent();
    }
    throw new IllegalStateException("Invalid ancestor <" + ancestor + "> for <" + node + ">");
  }

  /**
   * Finds all parents between the given node and the last parent.
   * The list does *not* contains the node, but *does* contain the last parent
   */
  @Nonnull
  public static List<? extends Node> getParents(@Nonnull Node child, @Nonnull Node lastParent) {
    ImmutableList.Builder<Node> parentsBuilder = ImmutableList.builder();

    @Nullable Node parent = child;
    while ((parent = parent.getParent()) != null) {
      parentsBuilder.add(parent);
      if (parent == lastParent) {
        return parentsBuilder.build();
      }
    }

    throw new IllegalStateException("Could not find ancestor <" + lastParent + "> for <" + child + ">");
  }
}
