package com.cedarsoft.annotations.instrumentation.test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.swing.SwingUtilities;

import org.fest.assertions.Assertions;
import org.fest.assertions.Fail;
import org.junit.*;

import com.cedarsoft.annotations.verification.DelegatingThreadVerificationStrategy;
import com.cedarsoft.annotations.verification.ThreadVerificationStrategy;
import com.cedarsoft.annotations.verification.VerifyThread;
import com.cedarsoft.test.utils.CatchAllExceptionsRule;
import com.google.common.collect.ImmutableList;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class RunnerClassTest {
  @Rule
  public CatchAllExceptionsRule catchAllExceptionsRule = new CatchAllExceptionsRule();

  @Before
  public void setUp() throws Exception {
    VerifyThread.setStrategy(new DelegatingThreadVerificationStrategy(ImmutableList.<ThreadVerificationStrategy>of()));
  }

  @After
  public void tearDown() throws Exception {
    VerifyThread.setStrategy(new DelegatingThreadVerificationStrategy(ImmutableList.<ThreadVerificationStrategy>of()));
  }

  public void setCustomThreadNameVerifier() throws Exception {
    VerifyThread.setStrategy(new ThreadVerificationStrategy() {
      @Override
      public void verifyThread(@Nonnull String... threadDescriptors) {
        Set<String> descriptorsSet = new HashSet<String>(Arrays.asList(threadDescriptors));
        if (!descriptorsSet.contains(Thread.currentThread().getName())) {
          throw new IllegalStateException("Invalid thread access from <" + Thread.currentThread().getName() + ">");
        }
      }
    });
  }

  @Test
  public void testCustomThreadAnnotFail() throws Exception {
    RunnerClass runnerClass = new RunnerClass();
    try {
      runnerClass.methodCustomThread();
      fail("Where is the Exception");
    } catch (Exception e) {
      Assertions.assertThat( e ).hasMessage( "Invalid thread access from <main>" );
    }
  }

  @Test
  public void testCustomThreadAnnot() throws Exception {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        RunnerClass runnerClass = new RunnerClass();
        runnerClass.methodCustomThread();
      }
    }, "my-custom-thread");
    thread.start();
    thread.join();
  }


  @Test
  public void testCustomThreadAnnotTest() throws Exception {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        daMethodCalledInCustomThread();
      }
    }, "my-custom-thread");
    thread.start();
    thread.join();
  }

  @CustomThreadAnnotation
  public void daMethodCalledInCustomThread() {
    System.out.println("com.cedarsoft.annotations.instrumentation.test.RunnerClassTest.daMethodCalledInCustomThread");
  }

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
  public void testNullMethodReturnValue() throws Exception {
    RunnerClass runnerClass = new RunnerClass();

    try {
      runnerClass.nonNullMethod();
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
      Assertions.assertThat( e ).isInstanceOf( java.lang.IllegalStateException.class );
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
