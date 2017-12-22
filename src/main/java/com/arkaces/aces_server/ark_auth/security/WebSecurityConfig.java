package com.arkaces.aces_server.ark_auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountUserDetailsService accountUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(customAuthenticationProvider)
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .httpBasic()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
                .antMatchers("/")
                    .permitAll()
                .antMatchers(HttpMethod.POST, "/accounts")
                    .permitAll()
                .anyRequest()
                    .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(accountUserDetailsService);
    }
  
}
