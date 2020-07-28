package com.zhangbo.onesaas.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * JWT登录授权过滤器
 */
@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = "";
        String password = "";
        if (request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            try (InputStream in = request.getInputStream()) {
                JwtUser jwtUser = new ObjectMapper().readValue(in, JwtUser.class);
                username = jwtUser.getUsername();
                password = jwtUser.getPassword();
            } catch (IOException e) {
                log.error("build login param error.", e);
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

}
