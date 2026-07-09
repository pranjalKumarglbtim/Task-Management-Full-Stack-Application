package com.taskmanagement.service;

import com.taskmanagement.dto.*;
import com.taskmanagement.entity.*;
import com.taskmanagement.exception.*;
import com.taskmanagement.repository.*;
import com.taskmanagement.security.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                      RoleRepository roleRepository,
                      RefreshTokenRepository refreshTokenRepository,
                      JwtTokenProvider jwtTokenProvider,
                      TokenBlacklistService tokenBlacklistService,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenBlacklistService = tokenBlacklistService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("USER");
                    return roleRepository.save(r);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);
        user.setEnabled(true);

        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().getName());
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        refreshTokenRepository.deleteByUserId(user.getId());
        RefreshToken rt = new RefreshToken();
        rt.setToken(refreshToken);
        rt.setUser(user);
        rt.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(rt);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().getName());
        return response;
    }

    public TokenRefreshResponse refresh(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

        if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Refresh token expired or revoked");
        }

        User user = storedToken.getUser();
        String newAccessToken = jwtTokenProvider.createAccessToken(user);

        TokenRefreshResponse response = new TokenRefreshResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public void logout(String token, String refreshToken) {
        Date expiration = jwtTokenProvider.getExpirationFromToken(token);
        long expiryTime = expiration.getTime() - System.currentTimeMillis();
        tokenBlacklistService.blacklistToken(token, expiryTime);

        if (refreshToken != null && !refreshToken.isEmpty()) {
            refreshTokenRepository.deleteByUserId(jwtTokenProvider.getUserIdFromToken(token));
        }
    }
}