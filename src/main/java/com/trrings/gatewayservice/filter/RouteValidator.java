package com.trrings.gatewayservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouteValidator {

    public static final List<String> openEndpoints = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/otp/send-otp",
            "/api/v1/user/user-get",
            "/api/v1/user/send-msg",
            "/api/v1/user/send-json-msg",
            "/api/v1/post/get-all",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/user-service/swagger-ui/**",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> openEndpoints.stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
