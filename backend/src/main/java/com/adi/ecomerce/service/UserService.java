package com.adi.ecomerce.service;

import com.adi.ecomerce.entities.User;
import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByEmail(String email); 

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
