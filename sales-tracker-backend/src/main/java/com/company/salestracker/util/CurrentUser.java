package com.company.salestracker.util;

import com.company.salestracker.entity.User;
import com.company.salestracker.exception.UnauthorizedException;
import com.company.salestracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CurrentUser {

    private final UserRepository userRepository;

    private Authentication auth() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !a.isAuthenticated()) {
            throw new UnauthorizedException("Not authenticated");
        }
        return a;
    }

    public boolean hasRole(String roleName) {
        for (GrantedAuthority ga : auth().getAuthorities()) {
            if (("ROLE_" + roleName).equals(ga.getAuthority())) 
            	return true;
        }
        return false;
    }

    public boolean isAdmin() {
        return hasRole("SUPER_ADMIN") || hasRole("ADMIN");
    }

    public boolean isAdminOrManager() {
        return isAdmin() || hasRole("SALES_MANAGER");
    }

    public boolean isOwnerScoped() {
        return !isAdminOrManager() && !hasRole("VIEWER");
    }

    public Integer getId() {
        String email = auth().getName();
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found: " + email));
        return u.getId();
    }
}
