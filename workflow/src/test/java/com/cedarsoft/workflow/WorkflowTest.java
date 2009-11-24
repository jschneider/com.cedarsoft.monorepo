package com.cedarsoft.workflow;

import junit.framework.Assert;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.lang.Override;

/**
 *
 */
public class WorkflowTest {
  private WorkflowDefinition<MyObject> workflowDefinition;
  private Workflow<MyObject> workflow;

  @BeforeMethod
  protected void setUp() throws Exception {
    workflowDefinition = new WorkflowDefinition<MyObject>( new State<MyObject>( "initialState" ) );
    workflow = workflowDefinition.createWorkflow( new MyObject() );

    workflowDefinition.getInitialState().createTransition( new State<MyObject>( "target1" ), new Action<MyObject>() {
      @Override
      public void execute( @NotNull MyObject object, @NotNull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "1" );
      }
    } );
    workflowDefinition.getInitialState().createTransition( new State<MyObject>( "target2" ), new Action<MyObject>() {
      @Override
      public void execute( @NotNull MyObject object, @NotNull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "first" );
      }
    }, new Action<MyObject>() {
      @Override
      public void execute( @NotNull MyObject object, @NotNull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "2" );
      }
    } );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testTransitions1() {
    assertNotNull( workflow );
    Assert.assertEquals( 2, workflow.getPossibleTransitionDefinitions().size() );
    Assert.assertEquals( 2, workflow.getPossibleTransition().size() );

    workflow.getPossibleTransition().get( 0 ).transit();
    Assert.assertEquals( "target1", workflow.getCurrentState().getId() );
    Assert.assertEquals( "1", workflow.getBean().getMessage() );
  }

  @Test
  public void testTransitions2() {
    assertNotNull( workflow );
    Assert.assertEquals( 2, workflow.getPossibleTransitionDefinitions().size() );
    Assert.assertEquals( 2, workflow.getPossibleTransition().size() );

    workflow.getPossibleTransition().get( 1 ).transit();
    Assert.assertEquals( "target2", workflow.getCurrentState().getId() );
    Assert.assertEquals( "2", workflow.getBean().getMessage() );
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
