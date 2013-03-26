package com.cedarsoft.annotations.instrumentation.test;

import org.fest.assertions.Assertions;
import org.fest.assertions.Fail;
import org.junit.*;

import javax.swing.SwingUtilities;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class RunnerClassTest {
  @Test
  public void testUiThread() throws Exception {
    SwingUtilities.invokeAndWait( new Runnable() {
      @Override
      public void run() {
        RunnerClass runnerClass = new RunnerClass();
        runnerClass.methodOnlyFromUi();

        try {
          runnerClass.methodOnlyFromNonUi();
          Fail.fail( "Where is the Exception" );
        } catch ( Exception e ) {
          Assertions.assertThat( e ).hasMessage( "Called from illegal thread. Must *not* be called from UI thread" );
        }
      }
    } );
  }

  @Test
  public void testNonUiThread() throws Exception {
    RunnerClass runnerClass = new RunnerClass();
    runnerClass.methodOnlyFromNonUi();

    try {
      runnerClass.methodOnlyFromUi();
      Fail.fail( "Where is the Exception" );
    } catch ( Exception e ) {
      Assertions.assertThat( e ).hasMessage( "Called from illegal thread. Must be called from UI thread" );
    }
  }

  @Test
  public void testNull() throws Exception {
    RunnerClass runnerClass = new RunnerClass();

    try {
      runnerClass.nonNullMethod("asdf");
      fail("Where is the Exception");
    } catch ( Exception e ) {
      Assertions.assertThat( e ).isInstanceOf( java.lang.IllegalStateException.class );
      Assertions.assertThat( e ).hasMessage( "Return value must not be null for method annotated with @Nonnull" );
    }
  }

  @Test
  public void testNonNull() throws Exception {
    RunnerClass runnerClass = new RunnerClass();

    String nullValue = runnerClass.nullMethod();
    assertThat( nullValue ).isNull();
  }
}
