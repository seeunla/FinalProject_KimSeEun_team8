package com.ll.exam.sen_books.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        csrf -> csrf.disable()
                )
//                .authorizeRequests(
//                        authorizeRequests -> authorizeRequests
//                                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/**")
//                                .hasAuthority("ADMIN")
//                                .anyRequest()
//                                .permitAll()
//                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login") // GET
                                .loginProcessingUrl("/member/login")
                )
                .logout(
                        logout -> logout.logoutUrl("/member/logout")
                                .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
