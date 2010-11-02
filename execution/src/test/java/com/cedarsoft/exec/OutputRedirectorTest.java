package com.cedarsoft.exec;

import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class OutputRedirectorTest {
  @Test( timeout = 100 )
  public void testDefault() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    OutputRedirector outputRedirector = new OutputRedirector( new ByteArrayInputStream( "asdf".getBytes() ), out );
    outputRedirector.run();

    assertEquals( "asdf", out.toString() );
  }


  @Test( timeout = 100 )
  public void testThreaded() throws InterruptedException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    OutputRedirector outputRedirector = new OutputRedirector( new ByteArrayInputStream( "asdf".getBytes() ), out );
    Thread thread = new Thread( outputRedirector );
    thread.start();

    thread.join();
    assertEquals( "asdf", out.toString() );
  }

  @Test
  public void testBlocking() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    OutputRedirector outputRedirector = new OutputRedirector( new BlockingInputStream(), out );
    Thread thread = new Thread( outputRedirector );
    thread.start();

    thread.join( 100 );
    assertTrue( thread.isAlive() );
    assertEquals( "*", out.toString() );

    outputRedirector.stop();
    thread.join( 100 );
    assertTrue( thread.isAlive() );
    thread.interrupt();
    thread.join( 100 );
    assertFalse( thread.isAlive() );
  }

  private static class BlockingInputStream extends InputStream {
    private volatile boolean blocking = false;

    @Override
    public int available() throws IOException {
      return 1;
    }

    @Override
    public int read( byte[] b, int off, int len ) throws IOException {
      if ( !blocking ) {
        blocking = true;
        b[0] = 42;
        return 1;
      }

      try {
        Thread.sleep( 10000000 );
      } catch ( InterruptedException e ) {
      }

      return -1;
    }

    @Override
    public int read() throws IOException {
      throw new UnsupportedOperationException();
    }
  }
}
