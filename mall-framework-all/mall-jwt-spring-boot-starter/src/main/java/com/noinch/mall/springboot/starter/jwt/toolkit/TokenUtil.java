package com.noinch.mall.springboot.starter.jwt.toolkit;

import com.noinch.mall.springboot.starter.jwt.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Token 工具类
 */
@Component
@AllArgsConstructor
public class TokenUtil {

    private final JwtProperties jwtProperties;

    /**
     * 获取秘钥实例
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 token
     * @param username 用户名
     * @return token
     */
    public String generateAccessToken(String username, String customerUserId) {
        return Jwts.builder()
                // 设置头部信息header
                .header()
                .add("typ", "JWT")
                .add("alg", jwtProperties.getAlg())
                .and()
                // 设置自定义负载信息payload
                .claim("username", username)
                .claim("customer-user-id", customerUserId)
                .id(UUID.randomUUID().toString())
                .expiration(Date.from(Instant.now().plusSeconds(jwtProperties.getExpire())))
                .issuedAt(new Date())   // 此令牌签发时间
                .subject(jwtProperties.getSubject())
                .issuer(jwtProperties.getIss())
                .signWith(getKey())   // 签名
                .compact();
    }

    /**
     * 解析 token
     * @param token token
     * @return Jws<Claims>
     */
    public Jws<Claims> parseClaim(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseClaim(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析 token 头部信息
     * @param token token
     * @return JwsHeader
     */
    public JwsHeader parseHeader(String token) {
        return parseClaim(token).getHeader();
    }

    /**
     * 解析 token 负载信息
     * @param token token
     * @return Claims
     */
    public Claims parsePayload(String token) {
        return parseClaim(token).getPayload();
    }

}
