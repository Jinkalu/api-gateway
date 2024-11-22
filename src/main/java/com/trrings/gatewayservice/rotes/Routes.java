package com.trrings.gatewayservice.rotes;

import com.trrings.gatewayservice.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {
    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routeConfig(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/user-service/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://user-service"))
                .route("user-service-swagger", r -> r.path("/user-service/v3/api-docs")
                        .filters(f -> f
                                .filter(authenticationFilter)
                                .rewritePath("/user-service/v3/api-docs", "/api-docs"))
                        .uri("lb://user-service"))
                .route("post-service", r -> r.path("/post-service/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://post-service"))
                .route("post-service-swagger", r -> r.path("/post-service/v3/api-docs")
                        .filters(f -> f
                                .filter(authenticationFilter)
                                .rewritePath("/post-service/v3/api-docs", "/api-docs"))
                        .uri("lb://post-service"))
                .build();
    }
}