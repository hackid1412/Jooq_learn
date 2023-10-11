package com.vn.jooq_learn.securities;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component

public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;

    public JwtTokenFilter(UserDetailsService userDetailsService, JwtTokenUtils jwtTokenUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if(isBypassToken(request)) {
                filterChain.doFilter(request, response); //enable bypass
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String email = jwtTokenUtils.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetail = (User) userDetailsService.loadUserByUsername(email);

                if (jwtTokenUtils.validateToken(token, userDetail)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetail,
                                    null,
                                    userDetail.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
    }

//    private boolean isBypassToken(HttpServletRequest request) {
//
//        final List<Pair<String, String>> bypassTokens = Arrays.asList(
//                Pair.of("api/v1/users", "GET"),
//                Pair.of("api/v1/users/create", "POST"),
//                Pair.of("api/v1/users/**", "PUT"),
//                Pair.of("api/v1/users/**", "PATCH"),
//                Pair.of("api/v1/users/", "DELETE")
//        );
//        for (Pair<String, String> bypassToken : bypassTokens) {
//            if (request.getServletPath().contains(bypassToken.getFirst()) &&
//                    request.getMethod().equals(bypassToken.getSecond())) {
//                return true;
//            }
//        }
//        return false;
//    }

    private boolean isBypassToken(HttpServletRequest request) {
//        final String bypassPath = "api/v1/users";
        final String byPass = "swagger-ui.html";
        return request.getServletPath().contains(byPass);
    }

}
