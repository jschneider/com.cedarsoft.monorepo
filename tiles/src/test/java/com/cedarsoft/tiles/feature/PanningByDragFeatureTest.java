package com.cedarsoft.tiles.feature;

import com.cedarsoft.concurrent.AccountingThreadService;
import com.cedarsoft.concurrent.NamedThreadFactory;
import com.cedarsoft.tiles.DebugTilesProvider;
import com.cedarsoft.tiles.MovementSpeedCalculator;
import com.cedarsoft.tiles.PanAndZoomModifier;
import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.View;
import com.cedarsoft.unit.other.px;
import com.google.common.collect.ImmutableList;
import org.junit.*;

import javax.annotation.Nonnull;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class PanningByDragFeatureTest {
  @Test
  public void testOneBigMovement() throws Exception {
    TilesComponent tilesComponent = new TilesComponent(new DebugTilesProvider(), ImmutableList.of(), ImmutableList.of(), new PanAndZoomModifier() {
      @Override
      public void modifyPanning(@Nonnull TilesComponent tilesComponent, @Nonnull @View @px Point2D.Double movement, double zoomFactorX, double zoomFactorY) {
      }

      @Override
      public double[] modifyZoomFactors(@Nonnull double[] zoomFactors) {
        return zoomFactors;
      }
    }, new AccountingThreadService(new NamedThreadFactory("dada")));

    PanningByDragFeature feature = new PanningByDragFeature(tilesComponent, InertialScrollingSpeedModifier.NONE);

    assertThat(feature.initialPressedEventX).isEqualTo(0);
    assertThat(feature.initialPressedEventY).isEqualTo(0);

    //mouse down
    feature.mousePressed(new MouseEvent(tilesComponent, 1, 0, 0, 100, 500, 1, false, MouseEvent.BUTTON1));

    assertThat(feature.initialPressedEventX).isEqualTo(100);
    assertThat(feature.initialPressedEventY).isEqualTo(500);

    //moved 100 pixels to the right
    feature.mouseDragged(new MouseEvent(tilesComponent, 2, 1, 0, 200, 500, 1, false, MouseEvent.BUTTON1));

    assertThat(feature.initialPressedEventX).isEqualTo(100);
    assertThat(feature.initialPressedEventY).isEqualTo(500);
    assertThat(feature.lastDragEventX).isEqualTo(200);
    assertThat(feature.lastDragEventY).isEqualTo(500);
    assertThat(feature.deadZoneCenterX).isNull();
    assertThat(feature.deadZoneCenterY).isEqualTo(500);

    //Mouse release
    feature.mouseReleased(new MouseEvent(tilesComponent, 3, 2, 0, 100, 500, 1, false, MouseEvent.BUTTON1));

    assertThat(feature.initialPressedEventX).isEqualTo(100);
    assertThat(feature.initialPressedEventY).isEqualTo(500);
    assertThat(feature.lastDragEventX).isEqualTo(200);
    assertThat(feature.lastDragEventY).isEqualTo(500);

    assertThat(feature.getSpeedCalculator().getMovements()).hasSize(1);
    MovementSpeedCalculator.Movement movement = feature.getSpeedCalculator().getMovements().iterator().next();
    assertThat(movement.getSpeedY()).isEqualTo(0);
    assertThat(movement.getSpeedX()).isEqualTo(100);
  }
}