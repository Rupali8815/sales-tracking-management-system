package com.company.salestracker.security;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class TokenRevocationService {

    private final Map<String, Long> loggedOutAtSeconds = new ConcurrentHashMap<>();

    public void revokeAllFor(String email) {
        loggedOutAtSeconds.put(email, System.currentTimeMillis() / 1000);
    }

    public boolean isRevoked(String email, Date issuedAt) {
        Long cutoff = loggedOutAtSeconds.get(email);
        return cutoff != null && issuedAt != null && issuedAt.getTime() / 1000 < cutoff;
    }
}
