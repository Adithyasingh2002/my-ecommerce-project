package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.User;
import com.adi.ecomerce.payload.AuthRequest;
import com.adi.ecomerce.payload.AuthResponse;
import com.adi.ecomerce.payload.RegisterRequest;
import com.adi.ecomerce.security.JwtUtil;
import com.adi.ecomerce.service.UserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${admin.secret}")
    private String adminSecret;
    @PostConstruct
    public void init() {
        logger.info("🔑 Loaded admin.secret = {}", adminSecret);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        logger.info("🔐 Login attempt for email: {}", request.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userService.getUserByEmail(request.getEmail());
            Set<String> roles = user.getRoles();

            String token = jwtUtil.generateToken(request.getEmail(), roles);
            String role = roles.iterator().next();

            logger.info("✅ Login successful for user: {} with role {}", request.getEmail(), role);
            AuthResponse authResponse = new AuthResponse(
                    token,
                    role,
                    user.getId(), 
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getAddress()
            );
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            logger.warn("❌ Invalid login credentials for: {}", request.getEmail());
            return ResponseEntity.status(401).body("❌ Invalid email or password");
        } catch (Exception e) {
            logger.error("⚠️ Internal error during login for {}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(500).body("⚠️ Internal error: " + e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("📝 Registering user with email: {}", request.getEmail());
        logger.info("📩 Payload: fullName={}, role={}, adminSecret={}",
                request.getFullName(), request.getRole(), request.getAdminSecret());

        try {
            if ("ADMIN".equalsIgnoreCase(request.getRole())) {
                if (adminSecret == null || !adminSecret.equals(request.getAdminSecret())) {
                    logger.warn("❌ Invalid admin secret for email: {}", request.getEmail());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ Invalid admin password");
                }
            }
            User user = new User();
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setAddress(request.getAddress());
            user.setPhoneNumber(request.getPhoneNumber());

            Set<String> roles = new HashSet<>();
            if ("ADMIN".equalsIgnoreCase(request.getRole())) {
                roles.add("ADMIN");
            } else {
                roles.add("USER");
            }
            user.setRoles(roles);
            User savedUser = userService.saveUser(user);
            logger.info("✅ Registration successful for {}", savedUser.getEmail());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            logger.error("⚠️ Registration failed for {}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(500).body("⚠️ Registration failed: " + e.getMessage());
        }
    }
}
