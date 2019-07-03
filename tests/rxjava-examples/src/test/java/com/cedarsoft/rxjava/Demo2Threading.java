package com.cedarsoft.rxjava;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Demo2Threading {
  /**
   * Demo um zu schauen, auf welchem Thread was genau passiert
   */
  @Test
  void threading1noConfiguration() throws Exception {
    LOG.info("Starting");

    Observable<String> myObservable = getNames();
    LOG.info("Got the observable");

    myObservable
      .subscribe(new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
          LOG.info("onSubscribe");
        }

        @Override
        public void onNext(String s) {
          LOG.info("on next <" + s + ">");
        }

        @Override
        public void onError(Throwable e) {
          LOG.error("Uups", e);
        }

        @Override
        public void onComplete() {
          LOG.info("on complete");
        }
      });


    //Warten bis alles erledigt ist
    Thread.sleep(1000);
  }

  /**
   * Wir möchten Kontrolle darüber haben, auf welchen Threads die Events eintrudeln
   */
  @Test
  void threading2threadingConfigured() throws Exception {
    LOG.info("Starting");

    Observable<String> myObservable = getNames();
    LOG.info("Got the observable");

    myObservable
      .observeOn(Schedulers.io())
      .subscribe(new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onNext(String s) {
          LOG.info("on next <" + s + ">");
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
          LOG.info("on complete");
        }
      });

    //Warten bis alles erledigt ist
    Thread.sleep(1000);
  }

  public Observable<String> getNames() {
    LOG.info("getNames called");
    //Just for now, (too) simple implementation
    return Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        LOG.info("Subscribe called");

        emitter.onNext("Bugs");

        new Thread(new Runnable() {
          @Override
          public void run() {
            emitter.onNext("Bunny");

            //Disposed?
            if (emitter.isDisposed()) {
              return;
            }

            emitter.onNext("Sylvester");
            emitter.onComplete();
          }
        }, "MyFunnyNameProducerThread").start();
      }
    });
  }

  private static final Logger LOG = LoggerFactory.getLogger(Demo2Threading.class.getName());
}
