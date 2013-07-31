package com.moandjiezana.vertx.integration;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;

public class ChatIntegrationTest extends TestVerticle {

  @Test
  public void should_do() throws Exception {
    container.deployVerticle("com.moandjiezana.vertx.chat.ChatVerticle", new Handler<AsyncResult<String>>() {
      @Override
      public void handle(AsyncResult<String> event) {
        vertx.createHttpClient().setPort(8080).get("/webjars/sockjs.min.js", new Handler<HttpClientResponse>() {
          @Override
          public void handle(HttpClientResponse response) {
            System.out.println(response.bodyHandler(new Handler<Buffer>() {

              @Override
              public void handle(Buffer buffer) {
                System.out.println(buffer.toString());
              }
            }));
            VertxAssert.testComplete();
          }
        }).end();
      }
    });
  }
}
