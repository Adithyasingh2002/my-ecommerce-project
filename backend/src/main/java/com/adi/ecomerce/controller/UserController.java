package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.User;
import com.adi.ecomerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // ✅ Allow registration if needed (or make admin-only)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Creating new user: {}", user.getEmail());
        User createdUser = userService.saveUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // ✅ Only allow ADMIN to fetch all users
    @GetMapping
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body("Access Denied: Admins only");
        }

        logger.info("Admin {} requested all users", authentication.getName());
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // ✅ Allow user to fetch only their own user data unless admin
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Principal principal, Authentication authentication) {
        try {
            User user = userService.getUserById(id);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"));

            if (!isAdmin && !user.getEmail().equals(principal.getName())) {
                return ResponseEntity.status(403).body("Access Denied: You can only view your own data");
            }

            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Allow update only if admin or same user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userUpdate,
                                        Principal principal, Authentication authentication) {
        try {
            User existingUser = userService.getUserById(id);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"));

            if (!isAdmin && !existingUser.getEmail().equals(principal.getName())) {
                return ResponseEntity.status(403).body("Access Denied: You can only update your own account");
            }

            User updatedUser = userService.updateUser(id, userUpdate);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Allow delete only if admin or same user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Principal principal, Authentication authentication) {
        try {
            User existingUser = userService.getUserById(id);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"));

            if (!isAdmin && !existingUser.getEmail().equals(principal.getName())) {
                return ResponseEntity.status(403).body("Access Denied: You can only delete your own account");
            }

            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
