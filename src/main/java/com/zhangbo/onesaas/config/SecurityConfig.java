package com.zhangbo.onesaas.config;


import com.zhangbo.onesaas.security.JwtFilter;
import com.zhangbo.onesaas.security.JwtLoginFilter;
import com.zhangbo.onesaas.security.UserDetailServiceImpl;
import com.zhangbo.onesaas.security.handler.LoginFailedHandler;
import com.zhangbo.onesaas.security.handler.LoginSuccessHandler;
import com.zhangbo.onesaas.security.handler.NotLoginDeniedEntryPointHandler;
import com.zhangbo.onesaas.security.handler.UnauthorizedAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private UnauthorizedAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailedHandler loginFailedHandler;

    @Autowired
    private UnauthorizedAccessDeniedHandler unauthorizedAccessDeniedHandler;

    @Autowired
    private NotLoginDeniedEntryPointHandler notLoginDeniedEntryPointHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtLoginFilter loginFilter() throws Exception {
        JwtLoginFilter loginFilter = new JwtLoginFilter();
        loginFilter.setAuthenticationManager(authenticationManager());
        return loginFilter;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()

                .and()
                .headers()
                .frameOptions().disable()

                .and()
                .authorizeRequests()
                //登录接口允许匿名访问
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                //其他接口全都需要鉴权认证
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                //登录成功
                .successHandler(loginSuccessHandler)
                //登录失败
                .failureHandler(loginFailedHandler)

                .and()
                //采用token，禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                //登录过滤器
                .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                //jwt过滤器
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                //未授权访问
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                //未认证访问
                .authenticationEntryPoint(notLoginDeniedEntryPointHandler)

                .and()
                //登出
                .logout().logoutUrl("/logout");

    }
}
