package com.cedarsoft.utils.workflow;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.lang.Override;

/**
 *
 */
public class WorkflowRealTest {
  private WorkflowDefinition<MyObject> workflowDefinition;

  @BeforeMethod
  protected void setUp() throws Exception {
    State<MyObject> input = new State<MyObject>( "input" );
    workflowDefinition = new WorkflowDefinition<MyObject>( input );

    State<MyObject> audit = new State<MyObject>( "audit" );
    input.createTransition( audit, new Action<MyObject>() {
      @Override
      public void execute( @NotNull MyObject object, @NotNull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "1" );
      }
    } );

    State<MyObject> bidding = new State<MyObject>( "bidding" );
    audit.createTransition( bidding );

    //Kauf
    State<MyObject> acquisition = new State<MyObject>( "acquisition" );
    bidding.createTransition( acquisition );

    State<MyObject> holding = new State<MyObject>( "holding" );
    acquisition.createTransition( holding );

    State<MyObject> selling = new State<MyObject>( "selling" );
    holding.createTransition( selling );

    State<MyObject> managing = new State<MyObject>( "managing" );
    selling.createTransition( managing );

    //Ablehnung
    bidding.createTransition( new State<MyObject>( "discarded" ) );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testCreate() {
    assertEquals( 1, workflowDefinition.getInitialState().getTransitions().size() );
  }

  @Test
  public void testWorkflow() {
    MyObject bean = new MyObject();

    Workflow<MyObject> workflow = new Workflow<MyObject>( bean, workflowDefinition );
    assertEquals( "input", workflow.getCurrentState().getId() );
    assertEquals( 1, workflow.getPossibleTransitionDefinitions().size() );

    workflow.getPossibleTransition().get( 0 ).transit();
    assertEquals( "audit", workflow.getCurrentState().getId() );

    workflow.getPossibleTransition().get( 0 ).transit();
    assertEquals( "bidding", workflow.getCurrentState().getId() );

    workflow.getPossibleTransition().get( 0 ).transit();
    assertEquals( "acquisition", workflow.getCurrentState().getId() );
  }

  @Test
  public void testActions() {
    MyObject object = new MyObject();
    Workflow<MyObject> workflow = workflowDefinition.createWorkflow( object );
    assertEquals( "input", workflow.getCurrentState().getId() );
    workflow.getPossibleTransition().get( 0 ).transit();

    assertEquals( "audit", workflow.getCurrentState().getId() );

    assertEquals( "1", object.getMessage() );
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
