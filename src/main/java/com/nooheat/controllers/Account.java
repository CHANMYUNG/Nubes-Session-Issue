package com.nooheat.controllers;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

/**
 * Created by NooHeat on 22/06/2017.
 */

@Controller("/session")
public class Account {

    @POST("/create")
    public void createSession(RoutingContext ctx, Session session){
        session.put("session","value");
        ctx.response().setStatusCode(201).end();
        ctx.response().close();
    }

    @GET("/check")
    public void checkSession(RoutingContext ctx, Session session){
        if(session.get("session") == null) {
            System.out.println("No session");
            ctx.response().setStatusCode(404).end("No session");
            ctx.response().close();
        }
        else{
            System.out.println((String)(session.get("session")));
            ctx.response().setStatusCode(200).end((String)session.get("session"));
            ctx.response().close();
        }
    }
}
