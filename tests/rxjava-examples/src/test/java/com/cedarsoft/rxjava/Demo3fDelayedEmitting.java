package com.cedarsoft.rxjava;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Dieses Demo zeigt das Verhalten, wenn die Ergebnisse mit einer Verzögerung emitted werden.
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Demo3fDelayedEmitting {
  @Test
  void testIt() throws Exception {

  }

  public Observable<String> getNames() {
    LOG.info("getFunnyNames called");

    return Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        LOG.info("Subscribe called");

        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              emitter.onNext("Bugs");

              //Simulate some kind of delay (e.g. IO)
              Thread.sleep(1000);
              emitter.onNext("Bunny");

              emitter.onComplete();
            }
            catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        }, "MyFunnyNameProducerThread").start();
      }
    });
  }

  private static final Logger LOG = LoggerFactory.getLogger(Demo3fDelayedEmitting.class.getName());
}
