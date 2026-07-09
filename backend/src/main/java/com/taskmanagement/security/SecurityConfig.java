package com.taskmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import com.taskmanagement.repository.UserRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    public SecurityConfig(UserRepository userRepository,
                         JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.userRepository = userRepository;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                                             UserDetailsService userDetailsService,
                                                             TokenBlacklistService tokenBlacklistService) {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, tokenBlacklistService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
 http
              .csrf(csrf -> csrf.disable())
              .cors(Customizer.withDefaults())
             .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .authorizeHttpRequests(auth -> auth
                 .requestMatchers("/auth/**").permitAll()
                 .requestMatchers("/tasks", "/tasks/**").permitAll()
                 .requestMatchers("/dashboard/**").permitAll()
                 .requestMatchers("/ws/**").permitAll()
                 .requestMatchers("/v3/api-docs/**").permitAll()
                 .requestMatchers("/swagger-ui/**").permitAll()
                 .requestMatchers("/actuator/**").permitAll()
                 .anyRequest().authenticated()
             );

        http.addFilterBefore(jwtAuthenticationFilter, 
            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}