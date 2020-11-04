package dev.mickael.message;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    log.info("Starting MainVerticle");
    HttpServer server = vertx.createHttpServer();

    RouterBuilder.create(vertx, getClass().getClassLoader().getResource("openapi.yml").getFile())
        .onComplete(
            ar -> {
              if (ar.succeeded()) {
                log.info("Successfully loaded OpenAPI file");
                var routerBuilder = ar.result();

                routerBuilder.operation("createAccount").handler(this::createAccountHandler);
                routerBuilder.operation("showAccount").handler(this::showAccountHandler);

                server.requestHandler(routerBuilder.createRouter());

                server.listen(
                    8080,
                    startServer -> {
                      if (startServer.succeeded()) {
                        startPromise.complete();
                      } else {
                        startPromise.fail(startServer.cause());
                      }
                    });
              } else {
                log.error("Could not load OpenAPI file", ar.cause());
                startPromise.fail(ar.cause());
              }
            });
  }

  private void createAccountHandler(RoutingContext ctx) {
    RequestParameters params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    RequestParameter body = params.body();
    JsonObject jsonBody = body.getJsonObject();

    log.info("received " + jsonBody.toString());

    var response = ctx.response();

    response.putHeader("content-type", "text/plain");
    response.end("Hello, Account!\n");
  }

  private void showAccountHandler(RoutingContext ctx) {
    var response = ctx.response();
    var accountID = ctx.pathParam("accountId");

    response.putHeader("content-type", "text/plain");
    response.end("Hello, show account " + accountID + "\n");
  }
}
