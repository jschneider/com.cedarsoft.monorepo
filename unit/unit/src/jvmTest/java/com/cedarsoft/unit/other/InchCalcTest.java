/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cedarsoft.unit.other;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.cedarsoft.unit.si.mm;


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
