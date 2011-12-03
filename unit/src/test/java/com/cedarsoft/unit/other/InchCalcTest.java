package com.cedarsoft.unit.other;

import com.cedarsoft.unit.si.mm;
import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class InchCalcTest {
  @Test
  public void testIt() throws Exception {
    @mm int mm = 100;
    @in double inInch = ( double ) mm / in.MM_RATIO;

    assertThat( inInch ).isEqualTo( 3.937007874015748 );
  }

  @Test
  public void testReturn() throws Exception {
    @in int inches = 1;
    @mm double mm = inches * in.MM_RATIO;
    assertThat( mm ).isEqualTo( 25.4 );
  }
}
