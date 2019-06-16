package com.cedarsoft.rxjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Demo2 {
  public Observable<String> getFunnyNames() {
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

  private static final Logger LOG = LoggerFactory.getLogger(Demo2.class.getName());
}
