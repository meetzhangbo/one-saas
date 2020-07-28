package com.zhangbo.onesaas.util;


import com.sun.tools.javac.util.Assert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private String secret;

    private Long expiration;

    @PostConstruct
    public void init() {
        Assert.checkNonNull(secret, "jwt secret is null");
        Assert.checkNonNull(expiration, "jwt expiration is null");
    }

    /**
     * 创建token令牌
     *
     * @param userDetails
     * @return
     */
    public String createJwtToken(UserDetails userDetails) {
        Map<String, Object> claimMap = new HashMap<>(2);
        claimMap.put("sub", userDetails.getUsername());
        claimMap.put("created", new Date());
        return createJwtToken(claimMap);
    }

    /**
     * 从token令牌中获取用户名
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    /**
     * 刷新token令牌
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        return null;
    }

    /**
     * 验证token令牌
     *
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    private String createJwtToken(Map<String, Object> map) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .addClaims(map)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims getClaimsFromToken(String token) {
        Claims body;
        try {
            body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            body = null;
        }
        return body;
    }
}
