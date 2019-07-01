package com.cedarsoft.rxjava;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Vortrag Java Forum 2019: Demo 1
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Demo1Basics {
  /**
   * Grundprinzip: Observable wird zurück gegeben.
   * Eigentlicher Code läuft noch nicht. Läuft erst verzögert bei Call zu subscribe auf.
   */
  @Test
  void basics1Observable() throws Exception {
    Observable<String> myObservable = getNames();
    //Der Code läuft nocht nicht!
    //Das Observable kann "konfiguriert" werden, falls gewünscht.

    myObservable.subscribe(); //Jetzt geht es (erst) los
  }

  /**
   * Und wie bekommen wir jetzt die Ergebnisse mitgeteilt?
   */
  @Test
  void basics2Observer() throws Exception {
    Observable<String> myObservable = getNames();

    //Einfachste Art sich als Observer zu registrieren
    //Ein Consumer ist ein einfaches Interface für "Umsteiger" - sehr ählich zu einem typischen Listener
    myObservable.subscribe(new Consumer<String>() {
      @Override
      public void accept(String s) throws Exception {
        System.out.println("Consumed <" + s + ">");
      }
    });

    //Und jetzt als Lambda
    myObservable.subscribe(s -> System.out.println("Consumed <" + s + ">"));
  }

  /**
   * Abbrechen - z.B. aufgrund einer UI-Aktion (Seite geschlossen, Aktion abgebrochen,
   * anderer Server hat schneller reagiert, ...)
   */
  @Test
  void basics3Disposable() throws Exception {
    Observable<String> myObservable = getNames();

    Disposable disposable = myObservable.subscribe(new Consumer<String>() {
      @Override
      public void accept(String s) throws Exception {
        System.out.println("Consumed <" + s + ">");
      }
    });

    //Wird aufgerufen, wenn z.B. der User weg navigiert
    disposable.dispose();
  }

  /**
   * Erweitertes Interface - mit mehr Callbacks für alle relevanten Zustände
   */
  @Test
  void basics4Observer() throws Exception {
    Observable<String> myObservable = getNames();

    //Wir registrieren ein Observer (anstatt eines Consumers weiter oben)
    myObservable.subscribe(new Observer<String>() {
      @Override
      public void onSubscribe(Disposable d) {
        //Wird am Anfang einmal aufgerufen. Hier kommt das Disposable an
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

  /**
   * Liefert das einfachst mögliche Observable zurück.
   * <p>
   * ACHTUNG: Diese Implementierung macht so keinen Sinn!!!
   * Dafür benötigt man kein RX-Java. Dann lieber eine ganz normale Liste nehmen
   */
  public Observable<String> getNames() {
    return Observable.fromArray("Bugs", "Bunny");
  }

  private static final Logger LOG = LoggerFactory.getLogger(Demo1Basics.class.getName());
}
