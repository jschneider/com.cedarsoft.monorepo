package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.si.ns;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Helper class that offers dragging related calculations
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DragSupport<T extends Node> {

  private boolean dragging;

  /**
   * The current location of the mouse pointer.
   * Is updated on every drag
   */
  @px
  private double mouseX;
  @px
  private double mouseY;
  /**
   * The time when the mouse coordinates have been updated
   */
  @ns
  private long mouseTime;


  @Nonnull
  private final T node;

  /**
   * If set to false, dragging is not allowed.
   * Only relevant when dragging is started
   */
  @Nonnull
  private final BooleanProperty draggingAllowed = new SimpleBooleanProperty(true);

  /**
   * Used to calculate the mouse speeds
   */
  @Nonnull
  private final MouseDragSpeedCalculator mouseDragSpeedCalculator = new MouseDragSpeedCalculator();

  public DragSupport(@Nonnull T node) {
    this.node = node;
  }

  public void install(@Nonnull Handler<T> handler) {
    this.node.setOnMouseDragged(event -> {
      if (!isDragging()) {
        if (!draggingAllowed.get()) {
          //dragging currently not allowed
          return;
        }

        dragging = true;

        updateMouseLocation(event);
        handler.dragStartDetected(node, mouseX, mouseY);
      }

      @px double deltaX = event.getSceneX() - mouseX;
      @px double deltaY = event.getSceneY() - mouseY;

      @ns long deltaTime = updateMouseLocation(event);
      mouseDragSpeedCalculator.add(deltaTime, deltaX, deltaY);

      handler.dragged(this.node, deltaX, deltaY);

      event.consume();
    });


    this.node.setOnMouseReleased(event -> {
      if (!isDragging()) {
        return;
      }

      dragging = false;

      handler.dragFinished(this.node, event.getX(), event.getY());

      event.consume();
    });
  }

  @ns
  private long updateMouseLocation(MouseEvent event) {
    mouseX = event.getSceneX();
    mouseY = event.getSceneY();

    @ns long now = System.nanoTime();
    @ns long delta = now - mouseTime;
    this.mouseTime = now;

    return delta;
  }

  public boolean isDragging() {
    return dragging;
  }

  @Nonnull
  public boolean isDraggingAllowed() {
    return draggingAllowed.get();
  }

  @Nonnull
  public BooleanProperty draggingAllowedProperty() {
    return draggingAllowed;
  }

  /**
   * Callback that is registered at the drag support
   */
  public interface Handler<T> {
    /**
     * The start of drag has been detected
     *
     * @param mouseX the x location within the dragged node
     * @param mouseY the y location within the dragged node
     */
    void dragStartDetected(@Nonnull T draggedNode, @px double mouseX, @px double mouseY);

    /**
     * Is called if a drag has been detected
     *
     * @param draggedNode the node that has been detected
     */
    void dragged(@Nonnull T draggedNode, @px double deltaX, @px double deltaY);

    /**
     * Is called if a drag has been finished
     */
    void dragFinished(@Nonnull T draggedNode, @px double x, @px double y);
  }
}
