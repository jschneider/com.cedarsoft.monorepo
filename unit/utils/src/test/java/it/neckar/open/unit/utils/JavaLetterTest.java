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
package it.neckar.open.unit.utils;

import static org.junit.Assert.*;

import org.junit.jupiter.api.*;

/**
 */
public class JavaLetterTest {
  @Test
  public void testName() throws Exception {
    assertTrue( Character.isJavaIdentifierStart( 'a' ) );
    assertTrue( Character.isDefined( '²' ) );
    assertTrue( Character.isValidCodePoint( '²' ) );

    assertFalse( Character.isJavaIdentifierPart( '²' ) );
    assertFalse( Character.isJavaIdentifierStart( '²' ) );
  }
}
