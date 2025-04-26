package com.clubvibeiq.backend.usermanagement.repository;

import com.clubvibeiq.backend.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
