package com.cedarsoft.unit;

import com.cedarsoft.unit.si.m;
import com.cedarsoft.unit.si.m2;
import com.cedarsoft.unit.si.m_s2;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UnitsTest {
  @Test
  public void testSymbol() {
    assertEquals( "m", Units.getSymbol( m.class ) );
    assertEquals( "m²", Units.getSymbol( m2.class ) );
    assertEquals( "m/s²", Units.getSymbol( m_s2.class ) );
  }

  @Test
  public void testName() throws Exception {
    assertEquals( "metre", Units.getName( m.class ) );
    assertEquals( "square metre", Units.getName( m2.class ) );
    assertEquals( "metre per square second", Units.getName( m_s2.class ) );
  }
}
