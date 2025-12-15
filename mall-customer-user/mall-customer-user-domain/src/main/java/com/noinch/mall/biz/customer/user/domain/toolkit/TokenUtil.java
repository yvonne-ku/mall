package com.noinch.mall.biz.customer.user.domain.toolkit;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Token 工具类
 */
public class TokenUtil {

    /**
     * 私钥
     */
    private final static String SECRET = "ODMwODExNzI1MzE0NTQ2MTQ3NjE4Mzc2MDE2MjczMDc=";

    /**
     * jwt签发者
     */
    private final static String JWT_ISS = "Noinch-Mall";

    /**
     * 秘钥实例
     */
    private final static SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * jwt主题
     */
    private final static String SUBJECT = "Mall-Login";

    /**
     * 过期时间(单位:秒)
     */
    public static final int ACCESS_EXPIRE = 60 * 60;


    /**
     * 生成 token
     * @param username 用户名
     * @return token
     */
    public static String genAccessToken(String username) {
        String uuid = UUID.randomUUID().toString();
        Date exprireDate = Date.from(Instant.now().plusSeconds(ACCESS_EXPIRE));
        return Jwts.builder()
                // 设置头部信息header
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                // 设置自定义负载信息payload
                .claim("username", username)
                .id(uuid)
                .expiration(exprireDate)
                .issuedAt(new Date())   // 此令牌签发时间
                .subject(SUBJECT)
                .issuer(JWT_ISS)
                .signWith(KEY)   // 签名
                .compact();
    }

    /**
     * 解析 token
     * @param token token
     * @return Jws<Claims>
     */
    public static Jws<Claims> parseClaim(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token);
    }

    public static JwsHeader parseHeader(String token) {
        return parseClaim(token).getHeader();
    }

    public static Claims parsePayload(String token) {
        return parseClaim(token).getPayload();
    }

}
