package com.pm.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange ->
                Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

    // IP = 111.112.1.1, COUNT=10  -> This is gets stored in Redis and is used by spring cloud gateway
    // to determine how to rate limit a request

}
