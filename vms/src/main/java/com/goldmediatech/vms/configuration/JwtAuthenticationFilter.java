package com.goldmediatech.vms.configuration;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.goldmediatech.vms.service.DefaultUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final DefaultUserDetailService userDetailService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, DefaultUserDetailService userDetailService) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Configurable list of public endpoints
        var publicEndpoints = List.of("/auth/login");
        var path = request.getServletPath();
        if (publicEndpoints.contains(path)) {
            log.debug("[AUTH] Public endpoint accessed: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // Default JWT logic
        var token = extractToken(request);
        if (StringUtils.hasText(token) && jwtUtil.isTokenValid(token)) {
            var username = jwtUtil.extractUserName(token);
            var userDetails = userDetailService.loadUserByUsername(username);
            var authentication = new UsernamePasswordAuthenticationToken(username, null,
                    userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("[AUTH] JWT valid, authentication set for user: {}", username);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.debug("[AUTH] No valid JWT, authentication not set");
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        var bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
