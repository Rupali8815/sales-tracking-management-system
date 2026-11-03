package com.company.salestracker.security;

import com.company.salestracker.entity.User;
import com.company.salestracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        // Authorities = role names + all permission codes mapped to those roles.
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> {
                    var roleAuthority = new SimpleGrantedAuthority("ROLE_" + role.getRoleName());
                    var permissionAuthorities = role.getPermissions().stream()
                            .map(p -> new SimpleGrantedAuthority(p.getPermissionCode()));
                    return java.util.stream.Stream.concat(java.util.stream.Stream.of(roleAuthority), permissionAuthorities);
                })
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(user.getStatus() != com.company.salestracker.enums.UserStatus.ACTIVE)
                .build();
    }
}
