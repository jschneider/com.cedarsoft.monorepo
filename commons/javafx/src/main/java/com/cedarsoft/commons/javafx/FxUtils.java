package com.cedarsoft.commons.javafx;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.unit.other.px;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FxUtils {
  /**
   * The amount of pixels that is added to ensure the node is not exactly at the edge
   */
  static final int SCROLL_TO_VISIBLE_MARGIN = 10;
  /**
   * Used for reflection workaround
   */
  private static final boolean IS_JDK_8 = System.getProperty("java.version").contains("1.8");

  /**
   * Returns the currently focused stage, or the one open stage or null if there is no stage or there are multiple stages
   */
  public static Stage getStage() {
    @Nullable Stage stageSafe = getStageSafe();
    if (stageSafe == null) {
      throw new IllegalStateException("No stage found");
    }

    return stageSafe;
  }

  /**
   * returns the stage (or null) if there is no stage
   */
  @Nullable
  public static Stage getStageSafe() {
    List<? extends Stage> stages = getStages();

    //Find the focused stage
    for (Stage stage : stages) {
      if (stage.isFocused()) {
        return stage;
      }
    }

    //we have no focused stage, therefore return the one stage if there is only one

    if (stages.size() == 1) {
      return stages.get(0);
    }

    //no stage found
    return null;
  }

  /**
   * Returns the available stages
   */
  public static List<? extends Stage> getStages() {
    try {

      if (IS_JDK_8) {
        //Code for JDK 8

        //Use reflection since StageHelper is not accesible with JDK > 8
        //
        // Replaces that code:
        // return StageHelper.getStages()
        Class<?> stageHelper = Class.forName("com.sun.javafx.stage.StageHelper");
        //noinspection unchecked
        return (List<? extends Stage>) stageHelper.getDeclaredMethod("getStages").invoke(null);
      }
      //Code for JDK 11

      //Use reflection since the method does not exist in JDK < 9
      //
      // Replaces that code:
      //windows = Stage.getWindows()

      //noinspection unchecked
      List<? extends Window> windows = (List<? extends Window>) Stage.class.getMethod("getWindows").invoke(null);

      return windows.stream()
               .filter(window -> window instanceof Stage)
               .map(window -> (Stage) window)
               .collect(ImmutableList.toImmutableList());

    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void dump(Node node, PrintStream out) {
    dump(node, out, 0);
  }

  private static void dump(Node node, PrintStream out, int depth) {
    out.println(Strings.repeat("  ", depth) + node + " #" + node.getId());

    Parent parent = (Parent) node;
    parent.getChildrenUnmodifiable()
      .forEach(child -> {
        dump(child, out, depth + 1);
      });
  }

  /**
   * Scrolls the given value to visible using an animation
   */
  static void scrollToVisible(@Nonnull ScrollPane scrollPane, double targetVValue, boolean animated) {
    if (!animated) {
      scrollPane.vvalueProperty().setValue(targetVValue);
      return;
    }

    final Timeline timeline = new Timeline();
    timeline.getKeyFrames().add(
      new KeyFrame(Duration.millis(200),
                   new KeyValue(scrollPane.vvalueProperty(), targetVValue, Interpolator.EASE_BOTH))
    );
    timeline.play();
  }

  /**
   * Scrolls the given node to visible within the first scroll pane that is found as ancestor
   */
  public static void scrollToVisible(@Nonnull Node node) {
    ScrollPane scrollPane = findParent(ScrollPane.class, node).orElseThrow(() -> new IllegalStateException("No parent scroll pane found"));
    scrollToVisible(scrollPane, node);
  }

  /**
   * Scrolls scroll pane to make the given node visible
   */
  public static void scrollToVisible(@Nonnull ScrollPane scrollPane, @Nonnull Node node) {
    Point2D positionInViewPort = localToAncestor(node, scrollPane.getContent(), 0, 0);
    @px double nodeHeight = node.getLayoutBounds().getHeight();

    scrollToVisible(scrollPane, positionInViewPort.getY(), nodeHeight);
  }

  /**
   * Scrolls the bar so that the target y is in the center of the visible area
   */
  public static void scrollToCenter(@Nonnull ScrollPane scrollPane, @px double targetY) {
    scrollToCenter(scrollPane, targetY, true);
  }

  public static void scrollToCenter(@Nonnull ScrollPane scrollPane, @px double targetY, boolean animated) {
    @px double height = scrollPane.getViewportBounds().getHeight();
    scrollToVisible(scrollPane, targetY - height / 2.0, height, 0, animated);
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
    @px double viewportHeight = scrollPane.getViewportBounds().getHeight();
    //The height of the content
    @px double contentHeight = scrollPane.getContent().getLayoutBounds().getHeight();
    //The height that is always hidden
    @px double hiddenHeight = contentHeight - viewportHeight;

    //The current min/max y values that are visible
    @px double currentlyVisibleTop = scrollPane.getVvalue() * hiddenHeight;
    @px double currentlyVisibleBottom = currentlyVisibleTop + viewportHeight;


    //The bottom y for the target area
    @px double targetYBottom = targetY + targetHeight;


    //Check for large target heights
    if (targetHeight > viewportHeight) {
      if (targetY < currentlyVisibleTop && targetYBottom > currentlyVisibleBottom) {
        //Do nothing, since some parts are visible
        return;
      }
    }


    if (targetY < currentlyVisibleTop) {
      //target y is above the currently visible area, we have to scroll
      double newVValue = minMax(0.0, 1.0, 1.0 / hiddenHeight * (targetY - scrollMargin));
      scrollToVisible(scrollPane, newVValue, animated);
      return;
    }


    if (targetYBottom > currentlyVisibleBottom) {
      //The target height is *not* larger than the viewport height, Just scroll
      double newVValue = minMax(0, 1.0, 1.0 / hiddenHeight * (targetYBottom - viewportHeight + scrollMargin));
      scrollToVisible(scrollPane, newVValue, animated);

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
    @Nullable Node currentNode = node;

    while (currentNode != null) {
      if (type.isAssignableFrom(currentNode.getClass())) {
        return Optional.of(type.cast(currentNode));
      }
      currentNode = currentNode.getParent();
    }

    return Optional.empty();
  }

  /**
   * Calculates the location relative to the ancestor (parent or parent of parent or parent of...)
   */
  public static Point2D localToAncestor(@Nonnull Node node, @Nonnull Node ancestor, double x, double y) {
    Point2D point2D = new Point2D(x, y);

    Node currentNode = node;
    while (currentNode != null) {
      if (currentNode == ancestor) {
        return point2D;
      }

      point2D = currentNode.localToParent(point2D);

      currentNode = currentNode.getParent();
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
