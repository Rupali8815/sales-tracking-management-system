package com.company.salestracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * AUTHORIZATION phase: decides *what the caller is allowed to do*.
 *
 * Runs immediately after {@link JwtAuthenticationFilter}. If that filter
 * resolved a valid identity (email, stashed as a request attribute), this
 * filter loads the full {@link UserDetails} — including the user's roles and
 * their mapped permission codes — via {@link CustomUserDetailsService}, and
 * populates the SecurityContext with those authorities.
 *
 * From here, Spring Security's {@code @PreAuthorize("hasAuthority('...')")}
 * annotations on controller/service methods can enforce RBAC using the
 * authorities set here.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String email = (String) request.getAttribute(JwtAuthenticationFilter.AUTHENTICATED_EMAIL_ATTRIBUTE);

        if (StringUtils.hasText(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
