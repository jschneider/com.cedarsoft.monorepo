package com.cedarsoft.annotations.instrumentation.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.assertj.core.api.Assertions;
import org.junit.*;

import com.cedarsoft.test.utils.CatchAllExceptionsRule;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class RunnerClassTest {
  @Rule
  public CatchAllExceptionsRule catchAllExceptionsRule = new CatchAllExceptionsRule();

  @Test
  public void testNullMethodReturnValue() throws Exception {
    RunnerClass runnerClass = new RunnerClass();

    try {
      runnerClass.nonNullMethod();
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
      Assertions.assertThat(e ).isInstanceOf(java.lang.IllegalStateException.class );
      Assertions.assertThat( e ).hasMessage( "Return value must not be null for method annotated with @Nonnull" );
    }
  }

  @Test
  public void testNullMethodParam1() throws Exception {
    RunnerClass runnerClass = new RunnerClass();

    runnerClass.nullMethod( "asdf", null ); //second param may be null

    try {
      runnerClass.nullMethod( null, "asdf" );
      fail("Where is the Exception");
    } catch ( Exception e ) {
      Assertions.assertThat( e ).isInstanceOf( IllegalArgumentException.class );
      Assertions.assertThat( e ).hasMessage( "Parameter 1 must not be null" );
    }
  }

  @Test
  public void testNonNull() throws Exception {
    RunnerClass runnerClass = new RunnerClass();

    String nullValue = runnerClass.nullMethod("asdf", "asdf");
    assertThat( nullValue ).isNull();
  }
}
