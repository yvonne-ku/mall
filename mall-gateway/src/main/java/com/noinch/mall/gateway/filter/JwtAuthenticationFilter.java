package com.noinch.mall.gateway.filter;

import com.noinch.mall.springboot.starter.jwt.toolkit.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * JWT认证网关过滤器：实现 GlobalFilter，且通过 Ordered 接口设置优先级为 -100（最高优先级）
 * 前端通过 nginx 将所有流量转发到网关，
 * 网关通过 JwtAuthenticationFilter 过滤器来验证请求中的 JWT 令牌，
 * 验证通过后，网关会将请求转发到后端服务，
 * 后端服务通过 X-User-Id 和 X-Username 头信息来识别用户。 */
@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final TokenUtil tokenUtil;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    // 公开路径（不需要认证的路径）
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/customer-user/login",
            "/api/customer-user/register",
            "/api/customer-user/verify/code/send",
            "/api/customer-user/geetestInit",
            "/swagger-ui",
            "/v3/api-docs",
            "/error"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // 1. 检查是否是公开路径
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // 2. 提取Token
        String token = extractToken(request);
        if (token == null) {
            return sendErrorResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "未提供认证令牌");
        }

        // 3. 验证Token
        try {
            Claims claims = tokenUtil.parsePayload(token);
            String username = claims.get("username", String.class);
            String customerUserId = claims.get("customerUserId", String.class);

            // 4. 将用户信息添加到请求头，传递给后端服务
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", customerUserId)
                    .header("X-Username", username)
                    .build();

            log.info("JWT认证成功 - 用户: {}, 路径: {}", username, path);
            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", token);
            return sendErrorResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "令牌已过期");
        } catch (JwtException e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return sendErrorResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "无效的令牌");
        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage());
            return sendErrorResponse(exchange.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误");
        }
    }

    @Override
    public int getOrder() {
        return -100; // 高优先级，最先执行
    }

    /**
     * 提取Token
     */
    private String extractToken(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get(TOKEN_HEADER);
        if (headers != null && !headers.isEmpty()) {
            String header = headers.getFirst();
            if (header.startsWith(TOKEN_PREFIX)) {
                return header.substring(TOKEN_PREFIX.length());
            }
        }
        return null;
    }

    /**
     * 检查是否是公开路径
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    /**
     * 发送错误响应（WebFlux方式）
     */
    private Mono<Void> sendErrorResponse(ServerHttpResponse response, HttpStatus status, String message) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String jsonResponse = String.format("{\"code\": %d, \"message\": \"%s\"}",
                status.value(), message);

        DataBuffer buffer = response.bufferFactory()
                .wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}