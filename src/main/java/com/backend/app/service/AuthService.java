package com.backend.app.service;

import com.backend.app.dto.AuthRequest;
import com.backend.app.dto.AuthResponse;
import com.backend.app.entity.User;
import com.backend.app.repository.UserRepository;
import com.backend.app.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static final Set<String> ALLOWED_ROLES = Set.of("ADMIN", "CUSTOMER");

    public ResponseEntity<String> register(AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email already in use!");
        }

        try {
            String role = request.getRole() != null ? request.getRole().toUpperCase() : "CUSTOMER";

            if (!ALLOWED_ROLES.contains(role)) {
                return ResponseEntity.badRequest().body("Invalid role specified. Allowed: " + ALLOWED_ROLES);
            }

            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .build();

            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully as " + role);

        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("Error: Could not register user. Duplicate or invalid data.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Registration failed due to server error.");
        }
    }

    public AuthResponse login(AuthRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = user.getRole();
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), role);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // Set cookies
        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) (jwtUtil.getAccessExpiration() / 1000));

        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) (jwtUtil.getRefreshExpiration() / 1000));

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return new AuthResponse(accessToken, refreshToken, role);
    }

    public AuthResponse refresh(String refreshToken, HttpServletResponse response) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String email = jwtUtil.extractUserId(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccessToken = jwtUtil.generateAccessToken(email, user.getRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        Cookie accessCookie = new Cookie("access_token", newAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) (jwtUtil.getAccessExpiration() / 1000));
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refresh_token", newRefreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) (jwtUtil.getRefreshExpiration() / 1000));
        response.addCookie(refreshCookie);

        return new AuthResponse(newAccessToken, newRefreshToken, user.getRole());
    }

    public String logout(HttpServletResponse response) {
        Cookie accessCookie = new Cookie("access_token", null);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);

        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return "Successfully logged out!";
    }
}
