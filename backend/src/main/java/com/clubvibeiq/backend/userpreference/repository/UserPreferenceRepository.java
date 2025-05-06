package com.clubvibeiq.backend.userpreference.repository;

import com.clubvibeiq.backend.userpreference.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, UUID> {
    List<UserPreference> findByClubIdAndIsActive(UUID clubId, Boolean isActive);
}
