package com.cedarsoft.tests.roach.motel;

import org.junit.*;


/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Ignore
public class RoachMotelTest {
  @Test
  public void it() throws Exception {

    for (int i = 0; i < 10000; i++) {
      RoachMotel roachMotel = new RoachMotel();

      Assert.assertEquals(0, roachMotel.a);
      Assert.assertFalse(roachMotel.called);
      Assert.assertEquals(0, roachMotel.c);


      new Thread(new Runnable() {
        @Override
        public void run() {
          roachMotel.doIt();
        }
      }).start();


      long start = System.nanoTime();

      while (true) {
        if (roachMotel.a == 0) {
          Assert.assertFalse(roachMotel.called);
          Assert.assertEquals(0, roachMotel.c);
        }

        if (roachMotel.called) {
          Assert.assertEquals(1, roachMotel.a);
        }

        if (roachMotel.c == 1) {
          Assert.assertEquals(1, roachMotel.a);
          Assert.assertTrue(roachMotel.called);
          break;
        }
      }

      System.out.println("Iteration took <" + (System.nanoTime() - start) + " ns>");
    }
  }

  /**
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  public static class RoachMotel {

    private static Object monitor = new Object();

    volatile int a;
    volatile boolean called;
    volatile int c;

    public void doIt() {
      a = 1;
      //synchronized (monitor) {
        called = true;
        someMethod();
      //}
      c = 1;
    }

    public boolean isCalled() {
      return called;
    }

    private static int someMethod() {
      int a = 0;
      for (int i = 0; i < 100000; i++) {
        a += i;
      }
      return a;
    }
  }
}