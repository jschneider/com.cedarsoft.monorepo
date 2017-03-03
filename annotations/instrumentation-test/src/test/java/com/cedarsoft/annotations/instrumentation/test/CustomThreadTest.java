package com.cedarsoft.annotations.instrumentation.test;

import com.cedarsoft.annotations.verification.DelegatingThreadVerificationStrategy;
import com.cedarsoft.annotations.verification.ThreadVerificationStrategy;
import com.cedarsoft.annotations.verification.VerifyThread;
import com.cedarsoft.test.utils.CatchAllExceptionsRule;
import com.google.common.collect.ImmutableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.assertj.core.api.Assertions;
import org.junit.*;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CustomThreadTest {
  @Rule
  public CatchAllExceptionsRule catchAllExceptionsRule = new CatchAllExceptionsRule();

  @Before
  public void setUp() throws Exception {
    VerifyThread.setStrategy( new ThreadVerificationStrategy() {
      @Override
      public void verifyThread( @Nonnull String... threadDescriptors ) {
        Set<String> descriptorsSet = new HashSet<String>( Arrays.asList( threadDescriptors ) );
        if ( !descriptorsSet.contains( Thread.currentThread().getName() ) ) {
          throw new IllegalStateException( "Invalid thread access from <" + Thread.currentThread().getName() + ">" );
        }
      }
    } );
  }

  @After
  public void tearDown() throws Exception {
    VerifyThread.setStrategy( new DelegatingThreadVerificationStrategy( ImmutableList.<ThreadVerificationStrategy>of() ) );
  }

  @Test
  public void testCustomThreadAnnotFail() throws Exception {
    RunnerClass runnerClass = new RunnerClass();
    try {
      runnerClass.methodCustomThread();
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
      Assertions.assertThat(e ).hasMessage("Invalid thread access from <main>" );
    }
  }

  @Test
  public void testCustomThreadAnnot() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        RunnerClass runnerClass = new RunnerClass();
        runnerClass.methodCustomThread();
      }
    }, "my-custom-thread" );
    thread.start();
    thread.join();
  }


  @Test
  public void testCustomThreadAnnotTest() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        daMethodCalledInCustomThread();
      }
    }, "my-custom-thread" );
    thread.start();
    thread.join();
  }

  @CustomThreadAnnotation
  public void daMethodCalledInCustomThread() {
    System.out.println( "com.cedarsoft.annotations.instrumentation.test.RunnerClassTest.daMethodCalledInCustomThread" );
  }
}
