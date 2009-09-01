package com.cedarsoft;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Simple template for mockito.
 */
public abstract class MockitoTemplate {

  /**
   * Stub the mocks
   *
   * @throws Exception
   */
  protected abstract void stub() throws Exception;

  /**
   * Execute the test code / assertions
   *
   * @throws Exception
   */
  protected abstract void execute() throws Exception;

  /**
   * Finally verify the mocks using {@link Mockito#verify(Object)}
   *
   * @throws Exception
   */
  protected abstract void verifyMocks() throws Exception;

  /**
   * Runs the tests
   *
   * @throws Exception
   */
  public void run() throws Exception {
    initMocks();
    stub();
    execute();
    verifyMocks();
  }

  protected void initMocks() {
    MockitoAnnotations.initMocks( this );
  }
}
