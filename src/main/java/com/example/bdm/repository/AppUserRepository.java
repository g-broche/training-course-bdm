package com.example.bdm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import com.example.bdm.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByEmail(String email);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByActivationToken(String activationToken);

    @Query("SELECT u.activationToken FROM AppUser u WHERE u.activationToken IS NOT NULL")
    Set<String> findAllNonNullActivationTokens();
}
