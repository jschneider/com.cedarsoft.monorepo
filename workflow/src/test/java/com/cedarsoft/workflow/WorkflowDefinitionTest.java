package com.cedarsoft.workflow;

import junit.framework.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class WorkflowDefinitionTest {
  private WorkflowDefinition<MyObject> workflowDefinition;
  private State<MyObject> initialState;

  @BeforeMethod
  protected void setUp() throws Exception {
    initialState = new State<MyObject>( "initialState" );
    workflowDefinition = new WorkflowDefinition<MyObject>( initialState );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testBuildNodes() {
    assertNotNull( workflowDefinition.getInitialState() );
    assertSame( initialState, workflowDefinition.getInitialState() );

    State<MyObject> otherState = new State<MyObject>( "otherState" );
    workflowDefinition.getInitialState().createTransition( otherState );
    Assert.assertEquals( 1, initialState.getTransitions().size() );
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
