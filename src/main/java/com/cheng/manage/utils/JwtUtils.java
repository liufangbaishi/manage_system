package com.cheng.manage.utils;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name JwtUtils
 * @date 2021/8/5 22:15
 */
@Slf4j
@Data
@Component
public class JwtUtils {

    @Value("${jwt.tokenHeader:Authorization}")
    private String tokenHeader; // Authorization

    @Value("${jwt.tokenHead:Bearer}")
    private String tokenHead; // Bearer

    @Value("${jwt.expiration:604800}")
    private long expiration; // token有效期，活跃用户不用重新登录

    @Value("${jwt.secret:cheng}")
    private String secret;

    /**
     * 生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("created", new Date());
        return generateToken(claims, userDetails.getUsername());
    }
    /**
     * 生成jwt
     * @return
     */
    public String generateToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public String getCurrentUserName(String token) {
        try {
            Claims claimByToken = getClaimByToken(token);
            // 获取用户名
            return claimByToken.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验token是否过期
     * @param authToken
     * @return
     */
    public boolean validateToken(String authToken) {
        Claims claims = getClaimByToken(authToken);
        return !isTokenExpired(claims);
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (StrUtil.isNotEmpty(token) && token.startsWith(tokenHead)) {
            token = token.replace(tokenHead, "").trim();
        }
        return token;
    }


    /**
     * 解析jwt
     * @param jwt
     * @return
     */
    private Claims getClaimByToken(String jwt) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 判断token是否过期
     * @param claims
     * @return  过期返回true
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
