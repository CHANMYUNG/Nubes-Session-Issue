package com.nooheat.core;

import com.github.aesteve.vertx.nubes.VertxNubes;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

import static com.github.aesteve.vertx.nubes.utils.async.AsyncUtils.completeOrFail;
import static com.github.aesteve.vertx.nubes.utils.async.AsyncUtils.ignoreResult;
import static com.github.aesteve.vertx.nubes.utils.async.AsyncUtils.onSuccessOnly;

/**
 * Created by NooHeat on 22/06/2017.
 */
public class CoreVerticle extends AbstractVerticle{

    protected io.vertx.core.http.HttpServer server;
    protected HttpServerOptions options;
    protected VertxNubes nubes;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        JsonObject config = context.config();
        config.put("src-package", "com.nooheat");
        options = new HttpServerOptions();
        options.setHost(config.getString("host", "localhost"));
        options.setPort(config.getInteger("port", 8004));
        nubes = new VertxNubes(vertx, config);
    }

    @Override
    public void start(Future<Void> future) {
        server = vertx.createHttpServer(options);
        nubes.bootstrap(onSuccessOnly(future, _router -> {
            server.requestHandler(_router::accept);
            server.listen(ignoreResult(future));
        }));
    }

    @Override
    public void stop(Future<Void> future) {
        nubes.stop(nubesRes -> closeServer(future));
    }

    private void closeServer(Future<Void> future) {
        if (server != null) {
            server.close(completeOrFail(future));
        } else {
            future.complete();
        }
    }
}
