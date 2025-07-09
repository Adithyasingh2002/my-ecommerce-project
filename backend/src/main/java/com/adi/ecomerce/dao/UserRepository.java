package com.adi.ecomerce.dao;

import com.adi.ecomerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findFirstByEmail(String email);
}
