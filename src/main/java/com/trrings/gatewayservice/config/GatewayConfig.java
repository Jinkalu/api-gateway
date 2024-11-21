package com.trrings.gatewayservice.config;

import com.trrings.gatewayservice.filter.AuthenticationFilter;
import com.trrings.gatewayservice.filter.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;

    @Autowired
    private RouteValidator routeValidator;

    @Bean
    public RouteLocator routeConfig(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service-filter", r -> r.path("/user-service/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .route("post-service-filter", r -> r.path("/post-service/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://post-service"))
                .route("openapi", r -> r.path("/v3/api-docs/**")
                        .filters(f -> f.filter(filter)
                                .rewritePath("/v3/api-docs/(?<segment>.*)", "/${segment}/v3/api-docs"))
                        .uri("lb://api-gateway"))
                .build();
    }


    @Bean
    public CorsWebFilter corsWebFilter(){
        final  CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
//        corsConfig.addAllowedOrigin("*");
        corsConfig.setAllowedOriginPatterns(Collections.singletonList("*"));
        final  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfig);
        return  new CorsWebFilter(source);
    }


/*  public static void main(String[] args) {
        try {
            // Generate a 256-bit HMAC SHA-256 secret key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(256); // Specify the key size
            SecretKey secretKey = keyGenerator.generateKey();

            // Encode the key in Base64 to store it securely
            String base64EncodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Generated Secret Key (Base64): " + base64EncodedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
