package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.UserRepository;
import com.adi.ecomerce.entities.User;
import com.adi.ecomerce.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        logger.info("Saving new user: {}", user.getEmail());
        User savedUser = userRepository.save(user);
        logger.debug("User saved: {}", savedUser);
        return savedUser;
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.debug("Total users found: {}", users.size());
        return users;
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    logger.debug("User found: {}", user);
                    return user;
                })
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID: " + id);
                });
    }

    @Override
    public User getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        return userRepository.findFirstByEmail(email)
                .map(user -> {
                    logger.debug("User found: {}", user);
                    return user;
                })
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new EntityNotFoundException("User not found with email: " + email);
                });
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        logger.info("Updating user with ID: {}", id);
        User existingUser = getUserById(id);

        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setRoles(updatedUser.getRoles());

        User updated = userRepository.save(existingUser);
        logger.debug("User updated: {}", updated);
        return updated;
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.error("User not found for deletion, ID: {}", id);
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        logger.debug("User deleted with ID: {}", id);
    }
}
