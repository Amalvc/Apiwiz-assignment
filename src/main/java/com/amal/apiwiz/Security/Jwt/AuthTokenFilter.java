package com.amal.apiwiz.Security.Jwt;

import com.amal.apiwiz.Exception.InvalidJwtTokenException;
import com.amal.apiwiz.Security.UserSecurity.UserDetailService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter class for handling authentication token validation.
 * This filter extracts the JWT token from the request header, validates it,
 * and sets the authentication in the SecurityContextHolder if the token is valid.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailService userDetailService;

    //Logger instance
    private static final Logger logger= LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * Filters the request to validate the authentication token.
     * If the token is valid, sets the authentication in the SecurityContextHolder.
     *
     * @param request - HTTP servlet request
     * @param response - HTTP servlet response
     * @param filterChain - Filter chain to proceed with the requests
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if Authorization header is empty or does not start with "Bearer"
        if (StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer")) {
            // If conditions are not met, continue with the filter chain
            filterChain.doFilter(request, response);
            return;
        }

        //extract jwt by removing "bearer"
        jwt = authHeader.substring(7);
        userEmail = jwtUtils.getUserName(jwt);

        if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);
            if (jwtUtils.isTokenValid(jwt, userDetails)) {
                // If token is valid, set authentication in the security context
                log.info("valid token");
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                log.debug("authorities " + userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }

        }
        filterChain.doFilter(request, response);
    }
}