package com.vn.jooq_learn.configs;

import com.vn.jooq_learn.securities.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableWebMvc
//@EnableMethodSecurity
public class WebSecurityConfig{
    private final JwtTokenFilter jwtTokenFilter;
    
//    public void configure(WebSecurity webSecurity) throws Exception {
//        webSecurity.ignoring().dispatcherTypeMatchers("/swagger-ui.html");
//    }


    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
        throws Exception{
        httpSecurity.csrf().disable().addFilterBefore(
                jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/users/**").permitAll()
                .anyRequest()
                .authenticated();
        httpSecurity.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });
        return httpSecurity.build();
    }
}
