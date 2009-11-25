package com.cedarsoft.tags.ui;

import org.testng.annotations.*;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

import static org.testng.Assert.*;

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
