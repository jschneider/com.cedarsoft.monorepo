package com.cedarsoft.tiles;

import com.cedarsoft.concurrent.AccountingThreadService;
import com.cedarsoft.concurrent.DefaultNewestOnlyJobManager;
import com.cedarsoft.concurrent.NamedThreadFactory;
import com.cedarsoft.tiles.feature.ActivatableStatsFeature;
import com.cedarsoft.tiles.feature.InertialScrollingSpeedModifier;
import com.cedarsoft.tiles.feature.KeyboardNavigationFeature;
import com.cedarsoft.tiles.feature.MouseWheelZoomFeature;
import com.cedarsoft.tiles.feature.PanningByDragFeature;
import com.google.common.collect.ImmutableList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executors;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TilesComponentDemo {
  public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    SwingUtilities.invokeAndWait(() -> {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      AccountingThreadService threadService = new AccountingThreadService(new NamedThreadFactory("tiles"));

      ImmutableList<Overlay> overlays = ImmutableList.of();
      ImmutableList<Overlay> underlays = ImmutableList.of();

      DefaultNewestOnlyJobManager jobsManager = new DefaultNewestOnlyJobManager(Executors.newFixedThreadPool(2, new NamedThreadFactory("GlobalOptionalJobManager")), 2);
      jobsManager.startWorkers();

      MultiThreadedObservableTilesProvider tilesProvider = new MultiThreadedObservableTilesProvider(new DelayingTilesProvider(new VolatileImagesTilesProvider(new DebugTilesProvider()), 100), jobsManager);

      TilesComponent tilesComponent = new TilesComponentWithStats(tilesProvider, overlays, underlays, PanAndZoomModifier.NONE, threadService);
      PanningByDragFeature.install(tilesComponent, InertialScrollingSpeedModifier.NONE);
      MouseWheelZoomFeature.install(tilesComponent);
      KeyboardNavigationFeature.install(tilesComponent, ZoomModifier.NONE);
      ActivatableStatsFeature.install(tilesComponent);

      frame.getContentPane().add(tilesComponent);

      frame.setSize(800, 600);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    });
  }
}