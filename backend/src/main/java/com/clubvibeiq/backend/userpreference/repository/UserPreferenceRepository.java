package com.clubvibeiq.backend.userpreference.repository;

import com.clubvibeiq.backend.userpreference.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, UUID> {

}
