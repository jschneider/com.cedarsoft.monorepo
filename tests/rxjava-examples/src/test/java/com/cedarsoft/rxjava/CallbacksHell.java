package com.cedarsoft.rxjava;

import java.io.IOException;

/**
 * Demo that shows what might happen with a lot of callbacks
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CallbacksHell {
  public static void main(String[] args) {
    String hostname = "myhost";

    //Guess port
    final int[] port = {80};

    //Try port 443
    get(hostname + ":443", new Callback() {
      @Override
      public void success(String content) {
        port[0] = 443;
      }

      @Override
      public void failure(String errorMessage) {
        //Guest it is not port 443, lets try 80
        get(hostname + ":80", new Callback() {
          @Override
          public void success(String content) {
            //port 80 it is!

            get("hostname:80/user-info", new Callback() {
              @Override
              public void success(String content) {
                //TOOD continue....
              }

              @Override
              public void failure(String errorMessage) {
                //Hmm. what to do now?
                throw new RuntimeException(new IOException("Could not find any user information: " + errorMessage));
              }
            });

          }

          @Override
          public void failure(String errorMessage) {
            //Hmm. what to do now?
            throw new RuntimeException(new IOException("Could not connect to host <" + hostname + "> due to <" + errorMessage + ">"));
          }
        });
      }
    });
  }

  public static void get(String url, Callback callback) {
  }


  public interface Callback {
    void success(String content);

    void failure(String errorMessage);
  }
}
