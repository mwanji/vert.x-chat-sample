package com.moandjiezana.vertx.webjars;

import org.apache.commons.io.IOUtils;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.mods.web.StaticFileHandler;
import org.vertx.mods.web.WebServerBase;
import org.webjars.WebJarAssetLocator;

public class WebJarsServer extends WebServerBase {

  @Override
  protected RouteMatcher routeMatcher() {
    final WebJarAssetLocator locator = new WebJarAssetLocator();
    RouteMatcher routeMatcher = new RouteMatcher();
    routeMatcher.getWithRegEx("\\/webjars\\/(.*)", new StaticFileHandler(vertx, "") {
      @Override
      public void handle(HttpServerRequest req) {
        String webJarPath = req.params().get("param0");
        String fullPath = locator.getFullPath(webJarPath);
        try {
          req.response().setChunked(true).end(IOUtils.toString(getClass().getClassLoader().getResourceAsStream(fullPath)));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
    routeMatcher.noMatch(staticHandler());
    return routeMatcher;
  }
}
