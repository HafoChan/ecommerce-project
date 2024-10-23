package com.sohan.api_gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohan.api_gateway.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    // Inject WebClient to communicate with user-service
    WebClient.Builder webClientBuilder;
    ObjectMapper objectMapper;

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestPath = exchange.getRequest().getPath().toString();
        HttpMethod requestMethod = exchange.getRequest().getMethod();

        if (isPublicEndpoint(requestPath, requestMethod))
            return chain.filter(exchange);

        // Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());

        String token = authHeader.get(0).replace("Bearer ", "");
        log.info("Token: {}", token);

        // verify token
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8081/auth/validate-token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)
                .then(chain.filter(exchange))
                .onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    private boolean isPublicEndpoint(String requestPath, HttpMethod method) {
        if (requestPath.matches(apiPrefix + "/user-service/.*"))
            return true;

        if (requestPath.matches(apiPrefix + "/product-service/categories/.*") && method == HttpMethod.GET)
            return true;

        if (requestPath.matches(apiPrefix + "/product-service/products/.*") && method == HttpMethod.GET)
            return true;

        return false;
    }

    // Xem thêm về reactive và webflux
    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .message("Unauthenticated")
                .build();

        String body;

        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
