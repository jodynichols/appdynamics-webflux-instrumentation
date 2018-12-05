package com.appdynamicsjn.webfluxdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RequestRouter {

    @Bean
    public RouterFunction<ServerResponse> route(RequestHandler requestHandler) {
        return RouterFunctions
                .route(GET("/search/{id}").and(accept(APPLICATION_JSON)), requestHandler::handleRequest);
    }
}