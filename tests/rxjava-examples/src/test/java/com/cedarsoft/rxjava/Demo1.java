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
public class Demo1 {
  @Test
  void basics1HelloWorld() throws Exception {
    Observable<String> myObservable = getFunnyNamesSimple();
    //No code running yet
    //"Configure" the observable (if you want to)

    myObservable.subscribe(); //Here they come....
  }

  @Test
  void basics2Disposable() throws Exception {
    Observable<String> myObservable = getFunnyNamesSimple();

    Disposable disposable = myObservable.subscribe();
    disposable.dispose();
  }

  @Test
  void basics3Observer() throws Exception {
    System.out.println("Starting");

    Observable<String> myObservable = getFunnyNamesSimple();
    System.out.println("Got the observable " + myObservable);

    myObservable.subscribe(new Observer<String>() {
      @Override
      public void onSubscribe(Disposable d) {
      }

      @Override
      public void onNext(String s) {
        System.out.println("Next: " + s);
      }

      @Override
      public void onError(Throwable e) {
        LOG.error("An error occurred", e);
      }

      @Override
      public void onComplete() {
        System.out.println("Completed");
      }
    });
  }

  @Test
  void basics4Threading() throws Exception {
    LOG.info("Starting");

    Observable<String> myObservable = getFunnyNames();
    LOG.info("Got the observable");

    myObservable
      .subscribeOn(Schedulers.computation())
      .observeOn(Schedulers.io())
      .subscribe(new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
          LOG.info("onSubscribe");
        }

        @Override
        public void onNext(String s) {
          LOG.info("on next");
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
          LOG.info("on complete");
        }
      });
  }

  public Observable<String> getFunnyNamesSimple() {
    return Observable.fromArray("Bugs", "Bunny");
  }

  public Observable<String> getFunnyNames() {
    LOG.info("getFunnyNames called");
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
            emitter.onComplete();
          }
        }, "MyFunnyNameProducerThread").start();
      }
    });
  }


  private static final Logger LOG = LoggerFactory.getLogger(Demo1.class.getName());
}
