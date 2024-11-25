package com.trrings.gatewayservice.filter;

import com.triings.trringscommon.exception.ApiError;
import com.triings.trringscommon.exception.ValidationException;
import com.trrings.gatewayservice.service.JWTAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/*@RefreshScope
@Configuration*/
@Component
public class AuthenticationFilter implements GatewayFilter, Ordered {


    @Autowired
    private RouteValidator validator;
    @Autowired
    private JWTAuthenticationService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        try {
            if (validator.isSecured.test(request)) {
                if (authMiss(request)) {
                 return Mono.error( new ValidationException(ApiError.builder()
                            .status(UNAUTHORIZED.name())
                            .httpStatus(UNAUTHORIZED)
                            .code(String.valueOf(UNAUTHORIZED.value()))
                            .errors(List.of("Authentication missing"))
                            .build()));
                }
                String token = request.getHeaders().getOrEmpty("Authorization").get(0);
                String jwt = token.substring(7);
                if (jwtService.isTokenExpired(jwt)) {
               return Mono.error( new ValidationException(ApiError.builder()
                            .status(UNAUTHORIZED.name())
                            .httpStatus(UNAUTHORIZED)
                            .code(String.valueOf(UNAUTHORIZED.value()))
                            .errors(List.of("Token expired"))
                            .build()));
                }
            }
        } catch (Exception ex) {
           return Mono.error( new ValidationException(ApiError.builder()
                    .status(UNAUTHORIZED.name())
                    .httpStatus(UNAUTHORIZED)
                    .code(String.valueOf(UNAUTHORIZED.value()))
                    .errors(List.of("Invalid token"))
                    .build()));
        }
/*        exchange.getRequest()
                .mutate()
                .header("X-Gateway-Token",jwtService.getApiKey())
                .build();*/

        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean authMiss(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }


    @Override
    public int getOrder() {
        return -1;
    }
}
