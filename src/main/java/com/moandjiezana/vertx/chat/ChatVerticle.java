package com.moandjiezana.vertx.chat;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.impl.JsonObjectMessage;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class ChatVerticle extends Verticle {

  private final CopyOnWriteArrayList<JsonObject> messages = new CopyOnWriteArrayList<>();

  @Override
  public void start() {
    Map<String, Object> serverConfig = new HashMap<>();
    serverConfig.put("web_root", "src/main/webapp");
    serverConfig.put("port", 8080);
    serverConfig.put("bridge", true);
    serverConfig.put("inbound_permitted", asList(new HashMap<String, Object>()));
    serverConfig.put("outbound_permitted", asList(new HashMap<String, Object>()));
    HashMap<String, Object> sjsConfig = new HashMap<>();
    sjsConfig.put("prefix", "/chat");
    serverConfig.put("sjs_config", sjsConfig);
    container.deployModule("io.vertx~mod-web-server~2.0.0-final", new JsonObject(serverConfig));

    vertx.eventBus().registerHandler("users/signIn", new Handler<JsonObjectMessage>() {
      @Override
      public void handle(JsonObjectMessage message) {
        container.logger().info("users/signIn: " + message.body());
        message.reply(new JsonObject().putNumber("status", 200));
        vertx.eventBus().publish("users/signedIn", message.body());
      }
    });

    vertx.eventBus().registerHandler("messages/post", new Handler<JsonObjectMessage>() {
      @Override
      public void handle(JsonObjectMessage message) {
        container.logger().info("messages/post: " + message.body());
        messages.add(message.body());
        vertx.eventBus().publish("messages/posted", message.body());
      }});

    container.logger().info("ChatVerticle started");
  }
}
