package com.cedarsoft.utils.workflow;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class TransitionsTest {
  @Test
  public void testAction() {
    State<MyObject> source = new State<MyObject>( "source" );
    TransitionDefinition<MyObject> transitionDefinition = new TransitionDefinition<MyObject>( source, new State<MyObject>( "target" ), new Action<MyObject>() {
      public void execute( @NotNull MyObject object, @NotNull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "1" );
      }
    } );

    MyObject object = new MyObject();
    assertEquals( "", object.getMessage() );

    Action<MyObject> action = transitionDefinition.getActions().get( 0 );
    assertNotNull( action );
    action.execute( object, transitionDefinition );
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
