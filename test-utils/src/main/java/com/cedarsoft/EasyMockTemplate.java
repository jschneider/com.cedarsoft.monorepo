package com.cedarsoft;

import net.sf.cglib.proxy.Enhancer;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class EasyMockTemplate {

  /**
   * Mock objects managed by this template
   */
  @NotNull
  private final List<Object> mocks = new ArrayList<Object>();

  /**
   * Constructor.
   *
   * @param mocks the mocks for this template to manage.
   * @throws IllegalArgumentException if the list of mock objects is <code>null</code> or empty.
   * @throws IllegalArgumentException if any of the given mocks is <code>null</code>.
   * @throws IllegalArgumentException if any of the given mocks is not a mock.
   */
  protected EasyMockTemplate( @NotNull Object... mocks ) {
    if ( mocks.length == 0 ) {
      throw new IllegalArgumentException( "The list of mock objects should not be empty" );
    }
    for ( Object mock : mocks ) {
      if ( mock == null ) {
        throw new IllegalArgumentException( "The list of mocks should not include null values" );
      }
      this.mocks.add( checkAndReturnMock( mock ) );
    }
  }

  private static Object checkAndReturnMock( @NotNull Object mock ) {
    if ( Enhancer.isEnhanced( mock.getClass() ) ) {
      return mock;
    }
    if ( Proxy.isProxyClass( mock.getClass() ) ) {
      return mock;
    }
    throw new IllegalArgumentException( mock + " is not a mock" );
  }

  /**
   * Encapsulates EasyMock's behavior pattern.
   * <ol>
   * <li>Set up expectations on the mock objects</li>
   * <li>Set the state of the mock controls to "replay"</li>
   * <li>Execute the code to test</li>
   * <li>Verify that the expectations were met</li>
   * </ol>
   * Steps 2 and 4 are considered invariant behavior while steps 1 and 3 should be implemented by subclasses of this
   * template.
   */
  public final void run() throws Exception {
    setUp();
    expectations();
    for ( Object mock : mocks ) {
      replay( mock );
    }
    codeToTest();
    for ( Object mock : mocks ) {
      verify( mock );
    }
  }

  /**
   * Sets the expectations on the mock objects.
   */
  protected abstract void expectations() throws Exception;

  /**
   * Executes the code that is under test.
   */
  protected abstract void codeToTest() throws Exception;

  /**
   * Sets up the test fixture if necessary.
   */
  protected void setUp() {
  }
}
