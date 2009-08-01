package com.cedarsoft.utils.tags.ui;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

/**
 *
 */
public class KeyStrokeTest {
  @Test
  public void testEnter() {
    KeyStroke stroke = KeyStroke.getKeyStroke( ( char ) KeyEvent.VK_ENTER );
    assertNotNull( stroke );
    assertEquals( '\n', stroke.getKeyChar() );
  }
}
