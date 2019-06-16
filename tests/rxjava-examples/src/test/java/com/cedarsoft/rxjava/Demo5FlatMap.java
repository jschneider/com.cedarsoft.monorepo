package com.cedarsoft.rxjava;

import java.net.URL;

import org.junit.jupiter.api.*;

import com.google.common.io.ByteStreams;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import javafx.util.Pair;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Demo5FlatMap {
  public Observable<String> getUrls() {
    return Observable.fromArray("https://cedarsoft.com", "https://cedarsoft.de");
  }

  @Test
  void testFlatMap() throws Exception {
    long start = System.currentTimeMillis();

    getUrls()
      .map(URL::new)
      .flatMap(url -> Single.fromCallable(() -> {
        Thread.sleep(1000);
        return url.openStream();
      }).toObservable())
      .map(inputStream -> new String(ByteStreams.toByteArray(inputStream)))
      .subscribe(o -> {
        System.out.println("Took: " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("---------> " + o.length() + " -- " + o.hashCode());
      })
    ;
  }

  @Test
  void testParallel() throws Exception {
    long start = System.currentTimeMillis();

    getUrls()
      .map(URL::new)
      .toFlowable(BackpressureStrategy.ERROR)
      .parallel()
      .runOn(Schedulers.io())
      .flatMap(url -> Single.fromCallable(() -> {
        Thread.sleep(1000);
        if (url.toString().contains("cedarsoft.com")) {
          Thread.sleep(1500);
        }

        return new Pair<>(url, url.openStream());
      }).toFlowable())
      .map(pair -> new Pair<>(pair.getKey(), new String(ByteStreams.toByteArray(pair.getValue()))))
      .sequential()
      .subscribe(pair -> {
        System.out.println("Result from " + pair.getKey());
        System.out.println("Took: " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("---------> " + pair.getValue().length() + " -- " + pair.getValue().hashCode());
      })
    ;

    Thread.sleep(10000);

  }
}
