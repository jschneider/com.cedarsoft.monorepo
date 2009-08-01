package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * <p/>
 * Date: Apr 27, 2007<br>
 * Time: 4:16:45 PM<br>
 */
public class RendererManagerTest {
  private RendererManager manager;

  @BeforeMethod
  protected void setUp() throws Exception {
    manager = new RendererManager();
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testGenerics() {
    manager.addRenderer( JLabel.class, new Renderer<JComponent, Object>() {
      @NotNull
      public String render( @NotNull JComponent obj, Object context ) {
        throw new UnsupportedOperationException();
      }
    } );

    Renderer<? super JLabel, Object> renderer = manager.getRenderer( JLabel.class );
    manager.addRenderer( JLabel.class, renderer );
  }
}
