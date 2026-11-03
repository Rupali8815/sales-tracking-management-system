package com.company.salestracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * AUTHENTICATION phase: proves *who the caller is*.
 *
 * Reads the "Authorization: Bearer <token>" header and checks the JWT itself
 * is present, well-formed, correctly signed, and not expired
 * (via {@link JwtTokenProvider#validateToken(String)}).
 *
 * It does NOT decide what the caller is allowed to do — it only resolves the
 * caller's identity (email) and hands it off via a request attribute to
 * {@link JwtAuthorizationFilter}, which loads the user's roles/permissions
 * and populates the SecurityContext.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Request attribute key used to pass the authenticated email to JwtAuthorizationFilter. */
    public static final String AUTHENTICATED_EMAIL_ATTRIBUTE = "authenticatedEmail";

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRevocationService tokenRevocationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            // A token issued before the user logged out is no longer accepted
            if (!tokenRevocationService.isRevoked(email, jwtTokenProvider.getIssuedAt(token))) {
                request.setAttribute(AUTHENTICATED_EMAIL_ATTRIBUTE, email);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
