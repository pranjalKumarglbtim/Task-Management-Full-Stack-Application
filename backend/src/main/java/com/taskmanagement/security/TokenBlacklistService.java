package com.taskmanagement.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklistToken(String token, long expiryTime) {
        blacklistedTokens.put(token, System.currentTimeMillis() + expiryTime);
    }

    public boolean isBlacklisted(String token) {
        Long expiryTime = blacklistedTokens.get(token);
        if (expiryTime != null) {
            if (System.currentTimeMillis() > expiryTime) {
                blacklistedTokens.remove(token);
                return false;
            }
            return true;
        }
        return false;
    }

    public void cleanupExpiredTokens() {
        blacklistedTokens.entrySet().removeIf(entry -> 
            System.currentTimeMillis() > entry.getValue());
    }
}