/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.workflow;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class WorkflowDefinitionTest {
  private WorkflowDefinition<MyObject> workflowDefinition;
  private State<MyObject> initialState;

  @BeforeEach
  public void setUp() throws Exception {
    initialState = new State<>("initialState");
    workflowDefinition = new WorkflowDefinition<>(initialState);
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testBuildNodes() {
    assertNotNull( workflowDefinition.getInitialState() );
    assertSame( initialState, workflowDefinition.getInitialState() );

    State<MyObject> otherState = new State<>("otherState");
    workflowDefinition.getInitialState().createTransition( otherState );
    assertEquals( 1, initialState.getTransitions().size() );
  }

  public static class MyObject {
    private String message = "";

    public String getMessage() {
      return message;
    }

    public void setMessage( String message ) {
      this.message = message;
    }
  }
}
