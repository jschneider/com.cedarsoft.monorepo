package com.cedarsoft.tiles.feature;

import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.ZoomModifier;
import com.cedarsoft.tiles.feature.action.PanDownAction;
import com.cedarsoft.tiles.feature.action.PanLeftAction;
import com.cedarsoft.tiles.feature.action.PanRightAction;
import com.cedarsoft.tiles.feature.action.PanUpAction;
import com.cedarsoft.tiles.feature.action.PanningStep;
import com.cedarsoft.tiles.feature.action.ZoomInAction;
import com.cedarsoft.tiles.feature.action.ZoomOutAction;

import javax.annotation.Nonnull;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Feature that enables keyboard navigation
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class KeyboardNavigationFeature {
  @Nonnull
  private static final Object ACTION_KEY_ZOOM_IN = "zoomIn";
  @Nonnull
  private static final Object ACTION_KEY_ZOOM_OUT = "zoomOut";
  @Nonnull
  private static final Object ACTION_KEY_DEFAULT_ZOOM = "defaultZoom";

  @Nonnull
  private static final Object ACTION_KEY_LEFT = "left";
  @Nonnull
  private static final Object ACTION_KEY_LEFT_LARGE = "leftLarge";
  @Nonnull
  private static final Object ACTION_KEY_RIGHT = "right";
  @Nonnull
  private static final Object ACTION_KEY_RIGHT_LARGE = "rightLarge";
  @Nonnull
  private static final Object ACTION_KEY_UP = "up";
  @Nonnull
  private static final Object ACTION_KEY_UP_LARGE = "uPLarge";
  @Nonnull
  private static final Object ACTION_KEY_DOWN = "down";
  @Nonnull
  private static final Object ACTION_KEY_DOWN_LARGE = "downLarge";

  @Nonnull
  private final TilesComponent tilesComponent;

  @Nonnull
  private final DefaultZoomAction defaultZoomAction;
  @Nonnull
  private final ZoomInAction zoomInAction;
  @Nonnull
  private final ZoomOutAction zoomOutAction;

  @Nonnull
  private final PanLeftAction panLeftAction;
  @Nonnull
  private final PanRightAction panRightAction;
  @Nonnull
  private final PanUpAction panUpAction;
  @Nonnull
  private final PanDownAction panDownAction;

  @Nonnull
  private final PanLeftAction panLeftLargeAction;
  @Nonnull
  private final PanRightAction panRightLargeAction;
  @Nonnull
  private final PanUpAction panUpLargeAction;
  @Nonnull
  private final PanDownAction panDownLargeAction;

  public KeyboardNavigationFeature(@Nonnull TilesComponent tilesComponent, @Nonnull ZoomModifier zoomModifier) {
    this.tilesComponent = tilesComponent;

    defaultZoomAction = new DefaultZoomAction(this.tilesComponent);
    zoomInAction = new ZoomInAction(this.tilesComponent, zoomModifier);
    zoomOutAction = new ZoomOutAction(this.tilesComponent, zoomModifier);

    panLeftAction = new PanLeftAction(tilesComponent, PanningStep.NORMAL);
    panRightAction = new PanRightAction(tilesComponent, PanningStep.NORMAL);
    panUpAction = new PanUpAction(tilesComponent, PanningStep.NORMAL);
    panDownAction = new PanDownAction(tilesComponent, PanningStep.NORMAL);

    panLeftLargeAction = new PanLeftAction(tilesComponent, PanningStep.LARGE);
    panRightLargeAction = new PanRightAction(tilesComponent, PanningStep.LARGE);
    panUpLargeAction = new PanUpAction(tilesComponent, PanningStep.LARGE);
    panDownLargeAction = new PanDownAction(tilesComponent, PanningStep.LARGE);

    fillInputMap();
    fillActionMap();
  }

  private void fillActionMap() {
    ActionMap actionMap = tilesComponent.getActionMap();

    actionMap.put(ACTION_KEY_DEFAULT_ZOOM, defaultZoomAction);
    actionMap.put(ACTION_KEY_ZOOM_OUT, zoomOutAction);
    actionMap.put(ACTION_KEY_ZOOM_IN, zoomInAction);

    actionMap.put(ACTION_KEY_LEFT, panLeftAction);
    actionMap.put(ACTION_KEY_RIGHT, panRightAction);
    actionMap.put(ACTION_KEY_UP, panUpAction);
    actionMap.put(ACTION_KEY_DOWN, panDownAction);

    actionMap.put(ACTION_KEY_LEFT_LARGE, panLeftLargeAction);
    actionMap.put(ACTION_KEY_RIGHT_LARGE, panRightLargeAction);
    actionMap.put(ACTION_KEY_UP_LARGE, panUpLargeAction);
    actionMap.put(ACTION_KEY_DOWN_LARGE, panDownLargeAction);
  }

  private void fillInputMap() {
    InputMap inputMap = tilesComponent.getInputMap();

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), ACTION_KEY_ZOOM_IN);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), ACTION_KEY_ZOOM_IN); //Num Pad +

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), ACTION_KEY_ZOOM_OUT);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), ACTION_KEY_ZOOM_OUT); //Num Pad -

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.CTRL_DOWN_MASK), ACTION_KEY_DEFAULT_ZOOM);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, InputEvent.CTRL_DOWN_MASK), ACTION_KEY_DEFAULT_ZOOM);

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), ACTION_KEY_LEFT);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK), ACTION_KEY_LEFT_LARGE);

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), ACTION_KEY_RIGHT);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), ACTION_KEY_RIGHT_LARGE);

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), ACTION_KEY_UP);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK), ACTION_KEY_UP_LARGE);

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), ACTION_KEY_DOWN);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK), ACTION_KEY_DOWN_LARGE);
  }

  public static void install(@Nonnull TilesComponent tilesComponent, @Nonnull ZoomModifier zoomModifier) {
    KeyboardNavigationFeature feature = new KeyboardNavigationFeature(tilesComponent, zoomModifier);
  }

}
